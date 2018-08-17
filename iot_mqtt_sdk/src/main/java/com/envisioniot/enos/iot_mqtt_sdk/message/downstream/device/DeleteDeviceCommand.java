package com.envisioniot.enos.iot_mqtt_sdk.message.downstream.device;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.ArrivedTopicPattern;
import com.envisioniot.enos.iot_mqtt_sdk.message.downstream.BaseMqttCommand;

import java.util.regex.Pattern;

/**
 * Created by yi.dai on 2018/7/11.
 */
public class DeleteDeviceCommand extends BaseMqttCommand<DeleteDeviceCommandReply>
{
    private static Pattern pattern = Pattern.compile(ArrivedTopicPattern.DELETE_DEVICE_COMMAND);

    @Override
    public Class<DeleteDeviceCommandReply> getAnswerType()
    {
        return DeleteDeviceCommandReply.class;
    }


    @Override
    public Pattern getMatchTopicPattern()
    {
        return pattern;
    }
}
