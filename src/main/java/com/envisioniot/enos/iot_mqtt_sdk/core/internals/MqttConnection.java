package com.envisioniot.enos.iot_mqtt_sdk.core.internals;

import com.envisioniot.enos.iot_mqtt_sdk.core.IConnectCallback;
import com.envisioniot.enos.iot_mqtt_sdk.core.IResponseCallback;
import com.envisioniot.enos.iot_mqtt_sdk.core.exception.EnvisionError;
import com.envisioniot.enos.iot_mqtt_sdk.core.exception.EnvisionException;
import com.envisioniot.enos.iot_mqtt_sdk.core.msg.IMqttDeliveryMessage;
import com.envisioniot.enos.iot_mqtt_sdk.core.msg.IMqttRequest;
import com.envisioniot.enos.iot_mqtt_sdk.core.msg.IMqttResponse;
import com.envisioniot.enos.iot_mqtt_sdk.core.profile.BaseProfile;
import com.envisioniot.enos.iot_mqtt_sdk.core.profile.DeviceCredential;
import com.envisioniot.enos.iot_mqtt_sdk.core.profile.FileProfile;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.ResponseCode;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.status.SubDeviceLoginRequest;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.status.SubDeviceLoginResponse;
import com.envisioniot.enos.iot_mqtt_sdk.util.SecureModeUtil;
import com.envisioniot.enos.iot_mqtt_sdk.util.StringUtil;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 *
 * @author zhensheng.cai
 * @date 2018/7/17.
 */
public class MqttConnection {
    private static final Logger logger = LoggerFactory.getLogger(MqttConnection.class);

    private AtomicLong requestId = new AtomicLong(0);
    private final MqttClient transport;
    //private final EnvClientContext clientContext;
    private DefaultProcessor mqttProcessor;
    private final MessageBuffer buffer;

    private final BaseProfile profile;
    private SubTopicCache subTopicCache = new SubTopicCache();
    private ExecutorFactory executorFactory;


    static class ConnectionTask {

        public FutureTask<Boolean> futureTask;

        public void setConnectTask(final MqttConnection connection, IConnectCallback callback) {
            this.futureTask = new FutureTask<Boolean>(() -> {
                connection.doConnect(callback);
                return true;
            });
        }

        public void setSubscribeTask(final MqttConnection connection, String topic ,int qos) {
            this.futureTask = new FutureTask<Boolean>(() -> {
                connection.doSubscribe(topic, qos);
                return true;
            });
        }

        /**
         * 顺序登录子设备，catch过程中的异常，只打印日志,当全部登录成功时返回true，否则false
         * @param connection
         * @param subDevices
         */
        public void setSubDeviceLoginTask(final MqttConnection connection, final List<DeviceCredential> subDevices) {
            this.futureTask = new FutureTask<>(() -> {
                boolean result = true;
                for (DeviceCredential subDevice : subDevices) {
                    SubDeviceLoginRequest request = SubDeviceLoginRequest.builder()
                            .setSubDeviceInfo(subDevice.productKey, subDevice.productSecret, subDevice.deviceKey, subDevice.deviceSecret)
                            .build();
                    connection.fillRequest(request);
                    request.check();
                    try {
                        SubDeviceLoginResponse rsp = connection.publish(request);
                        if (logger.isDebugEnabled()) {
                            logger.debug("auto login subDevice rsp {} ", rsp);
                        }
                        if( rsp.getCode() != ResponseCode.SUCCESS){
                            logger.warn("auto login subDevice failed , rsp {} , ", rsp);
                            continue;
                        }
                        if (request.getSecureMode() == SecureModeUtil.VIA_PRODUCT_SECRET
                                && StringUtil.isNotEmpty(rsp.getSubDeviceSecret())) {
                            connection.getProfile().updateOrAddSubDevice(new DeviceCredential(
                                    rsp.getSubProductKey(), null, rsp.getSubDeviceKey(), rsp.getSubDeviceSecret()));
                            if (connection.getProfile() instanceof FileProfile) {
                                try {
                                    ((FileProfile) connection.getProfile()).persistent();
                                } catch (IOException e) {
                                    logger.error("", e);
                                }
                            } else {
                                logger.warn("mqtt client login subDevice by dynamic-activate-method ,but cannot persist the activated reply , please handle the reply: {} ", rsp);
                            }
                        }

                    } catch (Exception e) {
                        logger.error("", e);
                        result = false;
                    }
                }
                return result;
            });
        }

        public FutureTask<Boolean> getFutureTask(){
            return futureTask;
        }
    }

    /**
     * -1 means wait forever
     */

    public MqttConnection(BaseProfile profile, MessageBuffer buffer, ExecutorFactory executorFactory) {
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
        newConnection.requestId = old.requestId;
        return newConnection;
    }


    public void notifyConnectSuccess(){
        this.executorFactory.getPublishExecutor().execute(buffer.createRepublishDisconnetedMessageTask());
        if(profile.getSubDevices()!= null && !profile.getSubDevices().isEmpty()) {
            ConnectionTask task = new ConnectionTask();
            task.setSubDeviceLoginTask(this, profile.getSubDevices());
            this.executorFactory.getPublishExecutor().execute(task.getFutureTask());
        }
    }




    public void connect() throws EnvisionException {
        this.connect(null);
    }

    BaseProfile getProfile(){
        return this.profile;
    }

    ExecutorFactory getExecutorFactory(){
        return this.executorFactory;
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


    private void doSubscribe(String topic , int qos ) throws MqttException {
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


    public void fillRequest(IMqttDeliveryMessage request) {
        if (StringUtil.isEmpty(request.getMessageId())) {
            request.setMessageId(String.valueOf(requestId.incrementAndGet()));
        }
        if(request instanceof IMqttRequest) {
            if (StringUtil.isEmpty(((IMqttRequest) request).getVersion())) {
                ((IMqttRequest) request).setVersion(BaseProfile.VERSION);
            }
        }
        if (StringUtil.isEmpty(request.getProductKey()) && StringUtil.isEmpty(request.getDeviceKey())) {
            request.setProductKey(profile.getProductKey());
            request.setDeviceKey(profile.getDeviceKey());
        }
    }
}
