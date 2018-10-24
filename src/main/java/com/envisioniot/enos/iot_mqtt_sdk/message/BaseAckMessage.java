package com.envisioniot.enos.iot_mqtt_sdk.message;

import com.envisioniot.enos.iot_mqtt_sdk.core.msg.IMqttAck;

/**
 * @author zhensheng.cai
 * @date 2018/8/14.
 */
public abstract class BaseAckMessage extends AckMessageBody implements IMqttAck {

    private static final long serialVersionUID = 3573085884899558133L;
}
