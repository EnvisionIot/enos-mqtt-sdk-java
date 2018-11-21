package com.envisioniot.enos.iot_mqtt_sdk.message.downstream.ota;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.ArrivedTopicPattern;
import com.envisioniot.enos.iot_mqtt_sdk.message.downstream.BaseMqttCommand;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Map;
import java.util.regex.Pattern;

public class DeviceOtaCommand extends BaseMqttCommand<DeviceOtaReply> {

    /**
     *
     */
    private static final long serialVersionUID = -1562255084521046172L;
    private static Pattern pattern = Pattern.compile(ArrivedTopicPattern.DEVICE_OTA_COMMAND);

    @Override
    public Pattern getMatchTopicPattern() {
        return pattern;
    }

    @Override
    public Class<DeviceOtaReply> getAnswerType() {
        return DeviceOtaReply.class;
    }

    public Map<String, String> getOtaParams() {
        Map<String, String> rst = new Gson().fromJson((String) this.getParams(),
                new TypeToken<Map<String, String>>() {
                }.getType());
        return rst;
    }
}
