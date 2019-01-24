package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.tsl;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.ArrivedTopicPattern;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.BaseMqttResponse;

import java.util.regex.Pattern;

/**
 * @author zhensheng.cai
 * @date 2019/1/23.
 */
public class MeasurepointPostBatchResponse extends BaseMqttResponse {


    private static final long serialVersionUID = -7559431496854795924L;

    public static final Pattern pattern = Pattern.compile(ArrivedTopicPattern.MEASUREPOINT_POST_BATCH_REPLY);

    @Override
    public Pattern getMatchTopicPattern() {
        return pattern;
    }

}
