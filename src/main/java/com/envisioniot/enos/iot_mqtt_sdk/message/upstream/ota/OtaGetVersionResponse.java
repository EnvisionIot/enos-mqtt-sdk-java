package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.ota;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.ArrivedTopicPattern;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.BaseMqttResponse;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author zhensheng.cai
 */
public class OtaGetVersionResponse extends BaseMqttResponse {
    private static final long serialVersionUID = 2639430948817597198L;
    private static Pattern pattern = Pattern.compile(ArrivedTopicPattern.GET_VERSION_TOPIC_REPLY);

    public List<Firmware> getFirmwareList() {
        List<Map<String, Object>> dataList = getData();
        List<Firmware> results = Lists.newArrayList();
        if (dataList != null) {
            for (Map<String, Object> data : dataList) {
                Firmware f = new Firmware();
                f.version = (String) data.get("version");
                f.signMethod = (String) data.get("signMethod");
                f.sign = (String) data.get("sign");
                f.fileUrl = (String) data.get("fileUrl");
                f.fileSize = (int) data.get("fileSize");
                results.add(f);
            }
        }
        return results;
    }

    @Override
    public Pattern getMatchTopicPattern() {
        return pattern;
    }
}
