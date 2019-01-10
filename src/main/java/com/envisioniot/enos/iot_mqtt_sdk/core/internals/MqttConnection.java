package com.envisioniot.enos.iot_mqtt_sdk.core.internals;

import com.envisioniot.enos.iot_mqtt_sdk.core.IConnectCallback;
import com.envisioniot.enos.iot_mqtt_sdk.core.IResponseCallback;
import com.envisioniot.enos.iot_mqtt_sdk.core.exception.EnvisionError;
import com.envisioniot.enos.iot_mqtt_sdk.core.exception.EnvisionException;
import com.envisioniot.enos.iot_mqtt_sdk.core.msg.IMqttDeliveryMessage;
import com.envisioniot.enos.iot_mqtt_sdk.core.msg.IMqttRequest;
import com.envisioniot.enos.iot_mqtt_sdk.core.msg.IMqttResponse;
import com.envisioniot.enos.iot_mqtt_sdk.core.profile.AbstractProfile;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * @author zhensheng.cai
 * @date 2018/7/17.
 */
public class MqttConnection {
    private static final Logger logger = LoggerFactory.getLogger(MqttConnection.class);

    private final MqttClient transport;
    //private final EnvClientContext clientContext;
    private DefaultProcessor mqttProcessor;
    private final MessageBuffer buffer;

    private final AbstractProfile profile;
    private SubTopicCache subTopicCache = new SubTopicCache();

    private ExecutorFactory executorFactory;


    public static class ConnectionTask {

        public FutureTask<Boolean> futureTask;

        public void setConnectTask(MqttConnection connection, IConnectCallback callback) {
            this.futureTask = new FutureTask<Boolean>(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    connection.doConnect(callback);
                    return true;
                }
            });
        }

        public void setSubscribeTask(MqttConnection connection, String topic ,int qos) {
            this.futureTask = new FutureTask<Boolean>(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    connection.doSubcribe(topic, qos);
                    return true;
                }
            });
        }

        public FutureTask<Boolean> getFutureTask(){
            return futureTask;
        }
    }


    /**
     * -1 means wait forever
     */

    public MqttConnection(AbstractProfile profile, MessageBuffer buffer, ExecutorFactory executorFactory) {
        this.profile = profile;
        this.buffer = buffer;
        this.executorFactory = executorFactory;
        try {
            this.transport = new MqttClient(profile.getServerUrl(), profile.getClientId(), new MemoryPersistence());
            this.mqttProcessor = new DefaultProcessor(profile, this);
            this.transport.setCallback(mqttProcessor);
            this.transport.setTimeToWait(profile.getTimeToWait() * 1000);
        } catch (MqttException e) {
            logger.error("", e);
            throw new RuntimeException(e);
        }
    }

    public MqttConnection recreate() throws EnvisionException {
        MqttConnection old = this;
        old.profile.reload();
        MqttConnection newConnection = new MqttConnection(old.profile, old.buffer , old.executorFactory);
        //在重新建立连接的过程中需要将Processor的状态量集成下来。
        newConnection.mqttProcessor.dumpProcessorState(old.mqttProcessor);
        return newConnection;
    }


    public void notifyConnectSuccess(){
        this.executorFactory.getPublishExecutor().execute(buffer.createRepublishDisconnetedMessageTask());
    }

    public void connect() throws EnvisionException {
        this.connect(null);
    }

    public void connect(IConnectCallback callback) throws EnvisionException {
        ConnectionTask task = new ConnectionTask();
        task.setConnectTask(this, callback);
        this.executorFactory.getConnectExecutor().execute(task.getFutureTask());
        try {
            task.getFutureTask().get(profile.getConnectionTimeout(), TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new EnvisionException(e.getMessage(), e.getCause(), EnvisionError.MQTT_CLIENT_CONNECT_FAILED);
        }
    }


    private void doConnect(IConnectCallback callback) throws EnvisionException {
        if (callback != null) {
            this.mqttProcessor.setConnectCallback(callback);
        }
        try {
            this.transport.connect(profile.createConnectOptions());
        } catch (MqttException e) {
            this.mqttProcessor.onConnectFailed(e.getReasonCode());
            logger.error("connect err.", e);
            throw new EnvisionException(e.getMessage(), e.getCause(), EnvisionError.MQTT_CLIENT_CONNECT_FAILED);
        }
    }

    public void disconnect() throws EnvisionException {
        try {
            this.transport.disconnect();
        } catch (MqttException e) {
            throw new EnvisionException(e.getMessage(), e.getCause(), EnvisionError.MQTT_CLIENT_DISCONNECT_FAILED);
        }
    }

    public String getClientId() {
        return this.transport.getClientId();
    }

    public void close() throws EnvisionException {
        try {
            this.transport.close();
        } catch (MqttException e) {
            throw new EnvisionException(e.getMessage(), e.getCause(), EnvisionError.MQTT_CLIENT_CLOSE_FAILED);
        }
    }

    public DefaultProcessor getProcessor() {
        return mqttProcessor;
    }


    public void fastPublish(IMqttDeliveryMessage request) throws Exception {
        this.doFastPublish(request);
    }


    private void doFastPublish(IMqttDeliveryMessage request) throws EnvisionException {
        try {
            request.check();
            if(transport.isConnected()) {
                if (request.getQos() == 1) {
                    transport.publish(request.getMessageTopic(), request.encode(), request.getQos(), false);
                }
                /**
                 * issue: https://github.com/eclipse/paho.mqtt.java/issues/421
                 */
                else if (request.getQos() == 0) {
                    synchronized (transport) {
                        transport.publish(request.getMessageTopic(), request.encode(), request.getQos(), false);
                    }
                } else {
                    throw new EnvisionException(EnvisionError.QOS_2_NOT_ALLOWED);
                }
            }
            else {
                buffer.putDisconnetedMessage(request);
            }

        } catch (MqttException e) {
            logger.error("publish message failed messageRequestId {} ", request.getMessageTopic());
            throw new EnvisionException(e.getMessage(), e.getCause(), EnvisionError.MQTT_CLIENT_PUBLISH_FAILED);
        }
    }


    public <T extends IMqttResponse> void publish(IMqttRequest<T> request, IResponseCallback<T> callback) throws Exception {
        String topic = request.getAnswerTopic();
        if (!subTopicCache.exists(topic)) {
            this.transport.subscribe(topic , request.getQos());
            subTopicCache.put(topic);
        }

        mqttProcessor.createCallbackTask(request, callback, transport.getTimeToWait());
    }

    public <T extends IMqttResponse> T publish(IMqttRequest<T> request) throws Exception {
        String topic = request.getAnswerTopic();
        if (!subTopicCache.exists(topic)) {
            ConnectionTask task = new ConnectionTask();
            task.setSubscribeTask(this, topic, request.getQos());
            this.executorFactory.getConnectExecutor().execute(task.getFutureTask());
            task.getFutureTask().get(this.profile.getTimeToWait(), TimeUnit.SECONDS);
            subTopicCache.put(topic);
        }
        FutureTask<T> future = mqttProcessor.createFutureTask(request);
        try {
            return future.get(transport.getTimeToWait(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            mqttProcessor.removeFutureTask(request);
            throw new EnvisionException(e.getMessage(), e.getCause(), EnvisionError.FUTURE_TASK_TIME_OUT);
        }
    }


    private void doSubcribe(String topic , int qos ) throws MqttException {
        this.transport.subscribe(topic , qos);
    }


    public boolean isConnected() {
        return transport != null && transport.isConnected();
    }

    public String getTransportClientId(){
        return transport.getClientId();
    }


    public void cleanSubscribeTopicCache(){
        this.subTopicCache.clean();
    }


}
