package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.tag;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.ArrivedTopicPattern;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.BaseMqttResponse;

import java.util.regex.Pattern;

/**
 * Description: update device tags
 *
 * @author zhonghua.wu
 * @create 2018-07-09 14:41
 */
public class TagUpdateResponse extends BaseMqttResponse {
    private static final long serialVersionUID = -7551738482591675818L;
    private static Pattern pattern = Pattern.compile(ArrivedTopicPattern.TAG_UPDATE_REPLY);

    @Override
    public Pattern getMatchTopicPattern() {
        return pattern;
    }
}
