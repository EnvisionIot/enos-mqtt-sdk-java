package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.status;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.ArrivedTopicPattern;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.BaseMqttResponse;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * Description: sub-device logout response response data contains sub-device
 * productKey and deviceKey
 *
 * @author zhonghua.wu
 * @create 2018-07-09 14:35
 */
public class SubDeviceLogoutResponse extends BaseMqttResponse {
    private static final long serialVersionUID = 8783872769820214468L;
    private static Pattern pattern = Pattern.compile(ArrivedTopicPattern.SUB_DEVICE_LOGOUT_REPLY);

    public String getSubProductKey() {
        Map<String, String> data = getData();
        return data.get("productKey");
    }

    public String getSubDeviceKey() {
        Map<String, String> data = getData();
        return data.get("deviceKey");
    }

    @Override
    public Pattern getMatchTopicPattern() {
        return pattern;
    }
}
