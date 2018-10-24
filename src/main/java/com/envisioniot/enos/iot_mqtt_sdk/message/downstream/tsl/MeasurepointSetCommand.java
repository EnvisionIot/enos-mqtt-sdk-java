package com.envisioniot.enos.iot_mqtt_sdk.message.downstream.tsl;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.ArrivedTopicPattern;
import com.envisioniot.enos.iot_mqtt_sdk.message.downstream.BaseMqttCommand;

import java.util.regex.Pattern;

/**
 * { "id": "123", "version": "1.0", "params": { "temperature": "30.5" },
 * "method": "thing.service.property.set" }
 *
 * @author zhensheng.cai
 * @date 2018/7/10.
 */
public class MeasurepointSetCommand extends BaseMqttCommand<MeasurepointSetReply> {
    private static final long serialVersionUID = -6511773492855053181L;
    private static Pattern pattern = Pattern.compile(ArrivedTopicPattern.MEASUREPOINT_SET_COMMAND);

    @Override
    public Class<MeasurepointSetReply> getAnswerType() {
        return MeasurepointSetReply.class;
    }

    @Override
    public Pattern getMatchTopicPattern() {
        return pattern;
    }
}
