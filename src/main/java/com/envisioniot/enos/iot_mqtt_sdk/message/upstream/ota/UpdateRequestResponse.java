package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.ota;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.ArrivedTopicPattern;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.BaseMqttResponse;

import java.util.regex.Pattern;

public class UpdateRequestResponse extends BaseMqttResponse {

    /**
     *
     */
    private static final long serialVersionUID = 2639430948817597198L;
    private static Pattern pattern = Pattern.compile(ArrivedTopicPattern.UPDATE_REQUEST_TOPIC_REPLY);

    @Override
    public Pattern getMatchTopicPattern() {
        return pattern;
    }
}
