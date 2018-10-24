package com.envisioniot.enos.iot_mqtt_sdk.core.msg;


/**
 * Plain Upstream Request including Elink and Raw request
 *
 * @author zhensheng.cai
 * @date 2018/7/11.
 */
public interface IMqttRequest<T extends IMqttResponse> extends IMqttDeliveryMessage, IAnswerable<T> {
    String getVersion();

    void setVersion(String version);

}
