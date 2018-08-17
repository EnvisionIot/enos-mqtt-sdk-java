package com.envisioniot.enos.iot_mqtt_sdk.message.downstream.device;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.ArrivedTopicPattern;
import com.envisioniot.enos.iot_mqtt_sdk.message.downstream.BaseMqttCommand;

import java.util.regex.Pattern;

/**
 * Created by yi.dai on 2018/7/11.
 */
public class DisableDeviceCommand extends BaseMqttCommand<DisableDeviceCommandReply>
{
    private static Pattern pattern = Pattern.compile(ArrivedTopicPattern.DISABLE_DEVICE_COMMAND);
    @Override
    public Class<DisableDeviceCommandReply> getAnswerType()
    {
        return DisableDeviceCommandReply.class;
    }

    @Override
    public Pattern getMatchTopicPattern()
    {
        return pattern;
    }
}
