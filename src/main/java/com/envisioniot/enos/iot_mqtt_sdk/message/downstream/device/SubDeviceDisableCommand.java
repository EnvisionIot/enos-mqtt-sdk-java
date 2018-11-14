package com.envisioniot.enos.iot_mqtt_sdk.message.downstream.device;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.ArrivedTopicPattern;
import com.envisioniot.enos.iot_mqtt_sdk.message.downstream.BaseMqttCommand;

import java.util.regex.Pattern;

/**
 * @author zhensheng.cai
 * @date 2018/11/12.
 */
public class SubDeviceDisableCommand extends BaseMqttCommand<SubDeviceDisableReply> {

    private static final long serialVersionUID = 5060068749317434659L;
    private static final Pattern pattern = Pattern.compile(ArrivedTopicPattern.SUB_DEVICE_DISABLE_COMMAND);

    @Override
    public Class<SubDeviceDisableReply> getAnswerType() {
        return SubDeviceDisableReply.class;
    }

    @Override
    public Pattern getMatchTopicPattern() {
        return pattern;
    }

}
