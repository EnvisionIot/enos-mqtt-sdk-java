package com.envisioniot.enos.iot_mqtt_sdk.core.msg;

import java.util.List;

public interface IMessageHandler<F extends IMqttArrivedMessage, R extends IMqttDeliveryMessage> {
    /**
     * the arrived msg handler to be register to the sdk
     *
     * @param arrivedMessage the arrived msg instance , it may instanceof {@link IMqttCommand} or {@link IMqttResponse}
     * @param argList        the topic args extract from the arrived topic , including productKey , deviceKey ,etc
     * @return the msg you want to reply to the cloud , if you do NOT want send msg , just return NULL
     * @throws Exception
     */
    R onMessage(F arrivedMessage, List<String> argList) throws Exception;
}
