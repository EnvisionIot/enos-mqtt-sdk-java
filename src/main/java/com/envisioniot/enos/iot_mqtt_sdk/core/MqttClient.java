package com.envisioniot.enos.iot_mqtt_sdk.core;

import com.envisioniot.enos.iot_mqtt_sdk.core.exception.EnvisionException;
import com.envisioniot.enos.iot_mqtt_sdk.core.internals.DefaultProcessor;
import com.envisioniot.enos.iot_mqtt_sdk.core.internals.ExecutorFactory;
import com.envisioniot.enos.iot_mqtt_sdk.core.internals.MessageBuffer;
import com.envisioniot.enos.iot_mqtt_sdk.core.internals.MqttConnection;
import com.envisioniot.enos.iot_mqtt_sdk.core.msg.*;
import com.envisioniot.enos.iot_mqtt_sdk.core.profile.AbstractProfile;
import com.envisioniot.enos.iot_mqtt_sdk.core.profile.DefaultActivateResponseHandler;
import com.envisioniot.enos.iot_mqtt_sdk.core.profile.DefaultProfile;
import com.envisioniot.enos.iot_mqtt_sdk.core.profile.FileProfile;
import com.envisioniot.enos.iot_mqtt_sdk.extension.ExtServiceFactory;
import com.envisioniot.enos.iot_mqtt_sdk.message.downstream.activate.DeviceActivateInfoCommand;
import com.envisioniot.enos.iot_mqtt_sdk.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Envision Mqtt Sdk
 * documentation {@see https://github.com/EnvisionIot/enos-iot-mqtt-java-sdk}
 *
 * @author zhensheng.cai
 * @date 2018/7/3.
 */
public class MqttClient {

    private static Logger logger = LoggerFactory.getLogger(MqttClient.class);

    private AtomicLong requestId = new AtomicLong(0);
    private MqttConnection connection;
    private AbstractProfile profile;
    private DefaultProcessor mqttProcessor;
    private MessageBuffer buffer = new MessageBuffer();
    private ExtServiceFactory serviceFactory = new ExtServiceFactory();
    private ExecutorFactory executorFactory;



    /**
     * @param uri          mqtt broker server uri
     * @param productKey   productKey of login device credential
     * @param deviceKey    deviceKey
     * @param deviceSecret deviceSecret
     */
    public MqttClient(String uri, String productKey, String deviceKey, String deviceSecret) {
        this(new DefaultProfile(uri, productKey, deviceKey, deviceSecret));
    }

    /**
     * init with client profile
     *
     * @param profile client config profile
     */
    public MqttClient(AbstractProfile profile) {
        this.profile = profile;
        this.executorFactory = new ExecutorFactory();
        this.connection = new MqttConnection(profile, buffer, executorFactory);
        this.buffer.setConnection(connection);
        if(profile.getSecureMode()== 3 ){
            //register dynamic activated response handler
            if(this.profile instanceof FileProfile) {
                this.setArrivedMsgHandler(DeviceActivateInfoCommand.class,
                        new DefaultActivateResponseHandler((FileProfile) this.profile , this));
            }
            else {
                logger.warn("mqtt client dynamic activate device ,please handle the reply message [{}]",
                        DeviceActivateInfoCommand.class.getSimpleName());
            }
        }
    }

    public AbstractProfile getProfile() {
        return this.profile;
    }

    /**
     * publish the request and NOT care the respsonse
     *
     * @throws Exception
     */
    public void fastPublish(IMqttDeliveryMessage request) throws Exception {
        fillRequest(request);
        request.check();
        this.connection.fastPublish(request);
    }

    /**
     * publish the sync request and wait for the response
     *
     * @throws Exception
     */
    public <T extends IMqttResponse> T publish(IMqttRequest<T> request) throws Exception {
        // 1. do register IMessageCallback
        fillRequest(request);
        request.check();
        // 2. do post
        return connection.publish(request);
    }

    /**
     * publish the request and register the callback , the callback will be called when rcv the response
     *
     * @throws Exception
     */
    public <T extends IMqttResponse> void publish(IMqttRequest<T> request, IResponseCallback<T> callback)
            throws Exception {
        this.fillRequest(request);
        request.check();

        connection.publish(request, callback);
    }

    /**
     * set the msg handler for specific arrived msg
     */
    public <T extends IMqttArrivedMessage , D extends IMqttDeliveryMessage> void setArrivedMsgHandler(Class<T> arrivedMsgCls, IMessageHandler<T, D> handler) {
        connection.getProcessor().setArrivedMsgHandler(arrivedMsgCls, handler);
    }

//    public MqttConnection getTransportConnection(){
//        return connection;
//    }
//
//    public void setTransportConnection(MqttConnection mqttConnection){
//        this.connection = mqttConnection;
//    }
//

    public ExtServiceFactory getExtServiceFactory(){
        return serviceFactory;
    }

    public void rebuildConnection() throws EnvisionException {
        MqttConnection newConnnection = this.connection.recreate();
        this.buffer.setConnection(newConnnection);
        MqttConnection old = this.connection;
        this.connection = newConnnection;
        //try reconnect
        if(old.isConnected()){
            try {
                old.disconnect();
            }
            finally {
                old.close();
            }
            this.connect(this.connection.getProcessor().getConnectCallback());
        }
    }

    /**
     * connect with the callback
     *
     * @param callback callback called when connect success, failed or connection lost
     * @throws EnvisionException
     */
    public void connect(IConnectCallback callback) throws EnvisionException {
        this.connection.connect(callback);
    }

    public void disconnect() throws EnvisionException {
        this.connection.disconnect();
    }

    public void close() throws EnvisionException {
        this.connection.close();
    }

    public boolean isConnected() {
        return this.connection.isConnected();
    }

    private void fillRequest(IMqttDeliveryMessage request) {
        if (StringUtil.isEmpty(request.getMessageId())) {
            request.setMessageId(String.valueOf(requestId.incrementAndGet()));
        }
        if(request instanceof IMqttRequest) {
            if (StringUtil.isEmpty(((IMqttRequest) request).getVersion())) {
                ((IMqttRequest) request).setVersion(AbstractProfile.VERSION);
            }
        }
        if (StringUtil.isEmpty(request.getProductKey()) && StringUtil.isEmpty(request.getDeviceKey())) {
            request.setProductKey(profile.getProductKey());
            request.setDeviceKey(profile.getDeviceKey());
        }
    }
}
