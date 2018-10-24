package com.envisioniot.enos.iot_mqtt_sdk.core;

/**
 * @author zhensheng.cai
 * @date 2018/8/15.
 */
public interface IConnectCallback {
    /**
     * Called when the connection to the server is completed successfully.
     */
    void onConnectSuccess();

    /**
     * Called when the client connection lost
     */
    void onConnectLost();

    /**
     * Called when the client connect failed
     *
     * @param reasonCode the reason code for this exception.
     *                   the code representing the reason for this exception. {@link org.eclipse.paho.client.mqttv3.MqttException}
     */
    void onConnectFailed(int reasonCode);
}
