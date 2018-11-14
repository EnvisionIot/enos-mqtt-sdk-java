package com.envisioniot.enos.iot_mqtt_sdk.message.downstream.device;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.ArrivedTopicPattern;
import com.envisioniot.enos.iot_mqtt_sdk.message.downstream.BaseMqttCommand;

import java.util.regex.Pattern;

/**
 * @author zhensheng.cai
 * @date 2018/11/12.
 */
public class SubDeviceDeleteCommand extends BaseMqttCommand<SubDeviceDeleteReply> {
    private static final long serialVersionUID = -6498822921921722596L;

    public static final Pattern pattern = Pattern.compile(ArrivedTopicPattern.SUB_DEVICE_DELETE_COMMAND);
    @Override
    public Class<SubDeviceDeleteReply> getAnswerType() {
        return SubDeviceDeleteReply.class;
    }

    @Override
    public Pattern getMatchTopicPattern() {
        return pattern;
    }
}
