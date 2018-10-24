package com.envisioniot.enos.iot_mqtt_sdk.core.msg;


/**
 * the down stream command from the command
 *
 * @author zhensheng.cai
 * @date 2018/7/11.
 */
public interface IMqttCommand<T> extends IMqttArrivedMessage, IAnswerable<T> {
    //<T extends IMqttCommandReply> Class<T> getResponseClass();
}
