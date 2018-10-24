package com.envisioniot.enos.iot_mqtt_sdk.core.msg;

public interface IMqttMessage {
    String getMessageId();

    String getMessageTopic();
//    String getVersion();

    void setMessageTopic(String topic);

    void setMessageId(String msgId);
//    void setVersion(String version);


//    String getMethod();
//    Object getParams();

    String getProductKey();

    String getDeviceKey();

    void setProductKey(String productKey);

    void setDeviceKey(String deviceKey);

}
