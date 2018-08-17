package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.tag;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.ArrivedTopicPattern;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.BaseMqttResponse;

import java.util.regex.Pattern;

/**
 * Description: delete device tags
 *
 * @author zhonghua.wu
 * @create 2018-07-09 14:41
 */
public class DeleteDeviceTagResponse extends BaseMqttResponse
{
    private static Pattern pattern = Pattern.compile(ArrivedTopicPattern.TAG_DELETE_REPLY);
    @Override
    public Pattern getMatchTopicPattern()
    {
        return pattern;
    }

}
