package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.tag;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.ArrivedTopicPattern;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.BaseMqttResponse;

import java.util.regex.Pattern;

/**
 * @author zhensheng.cai
 * @date 2018/11/12.
 */
public class TagQueryResponse extends BaseMqttResponse{
    private static final long serialVersionUID = 5045978808520644002L;
    private static final Pattern pattern = Pattern.compile(ArrivedTopicPattern.TAG_QUERY_REPLY);

    @Override
    public Pattern getMatchTopicPattern() {
        return pattern;
    }

}
