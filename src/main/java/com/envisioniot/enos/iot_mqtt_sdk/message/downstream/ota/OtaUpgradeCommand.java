package com.envisioniot.enos.iot_mqtt_sdk.message.downstream.ota;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.ArrivedTopicPattern;
import com.envisioniot.enos.iot_mqtt_sdk.message.downstream.BaseMqttCommand;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.ota.Firmware;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.regex.Pattern;

public class OtaUpgradeCommand extends BaseMqttCommand<OtaUpgradeReply> {
    private static final long serialVersionUID = -1562255084521046172L;
    private static Pattern pattern = Pattern.compile(ArrivedTopicPattern.DEVICE_OTA_COMMAND);

    @Override
    public Pattern getMatchTopicPattern() {
        return pattern;
    }

    @Override
    public Class<OtaUpgradeReply> getAnswerType() {
        return OtaUpgradeReply.class;
    }

    public List<Firmware> getAvailableFirmwareList() {
        List<Firmware> rst = new Gson().fromJson((String) this.getParams(),
                new TypeToken<List<Firmware>>() {
                }.getType());
        return rst;
    }
}
