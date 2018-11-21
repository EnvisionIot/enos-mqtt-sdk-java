package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.ota;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.ArrivedTopicPattern;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.BaseMqttResponse;

import java.util.regex.Pattern;

public class OtaVersionReportResponse extends BaseMqttResponse {

    /**
     *
     */
    private static final long serialVersionUID = 5454853289876001313L;
    private static Pattern pattern = Pattern.compile(ArrivedTopicPattern.VERSION_REPORT_TOPIC_REPLY);

    @Override
    public Pattern getMatchTopicPattern() {
        return pattern;
    }
}
