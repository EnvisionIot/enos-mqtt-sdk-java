package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.tsl;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.ArrivedTopicPattern;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.BaseMqttResponse;

import java.util.regex.Pattern;

/**
 * @author zhensheng.cai
 * @date 2018/7/10.
 */
public class ModelUpRawResponse extends BaseMqttResponse
{

    private static final long serialVersionUID = -3130733082784463467L;
    private static Pattern pattern = Pattern.compile(ArrivedTopicPattern.MODEL_UP_RAW_REPLY);

    @Override
    public Pattern getMatchTopicPattern()
    {
        return pattern;
    }
}
