package com.envisioniot.enos.iot_mqtt_sdk.message.downstream.device;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.ArrivedTopicPattern;
import com.envisioniot.enos.iot_mqtt_sdk.message.downstream.BaseMqttCommand;

import java.util.regex.Pattern;

/**
 * @author zhensheng.cai
 * @date 2018/11/12.
 */
public class SubDeviceEnableCommand extends BaseMqttCommand<SubDeviceEnableReply> {
    private static final long serialVersionUID = 7453610985434887424L;

    private static final Pattern pattern = Pattern.compile(ArrivedTopicPattern.SUB_DEVICE_ENABLE_COMMAND);

    @Override
    public Class<SubDeviceEnableReply> getAnswerType() {
        return SubDeviceEnableReply.class;
    }

    @Override
    public Pattern getMatchTopicPattern() {
        return pattern;
    }
}
