package com.envisioniot.enos.iot_mqtt_sdk.core.msg;

public interface IMqttAck {
    void setCode(int code);

    void setMessage(String message);
}
