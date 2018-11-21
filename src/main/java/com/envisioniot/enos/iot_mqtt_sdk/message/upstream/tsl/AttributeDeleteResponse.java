package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.tsl;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.ArrivedTopicPattern;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.BaseMqttResponse;

import java.util.regex.Pattern;

/**
 * @author zhensheng.cai
 * @date 2018/11/21.
 */
public class AttributeDeleteResponse extends BaseMqttResponse {

    public static final Pattern pattern = Pattern.compile(ArrivedTopicPattern.ATTRIBUTE_DELETE_REPLY);

    @Override
    public Pattern getMatchTopicPattern() {
        return pattern;
    }

}
