package com.envisioniot.enos.iot_mqtt_sdk.message;

import com.envisioniot.enos.iot_mqtt_sdk.core.msg.IAnswerable;

/**
 * @author zhensheng.cai
 * @date 2018/8/14.
 */
public abstract class BaseAnswerableMessage<T> extends AnswerableMessageBody implements IAnswerable<T> {

    private static final long serialVersionUID = -1174370764393046305L;
}
