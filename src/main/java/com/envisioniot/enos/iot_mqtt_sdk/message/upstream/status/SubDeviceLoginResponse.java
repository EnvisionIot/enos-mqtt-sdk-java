package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.status;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.ArrivedTopicPattern;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.BaseMqttResponse;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * Description: sub-device login response response data contains sub-device
 * productKey and deviceKey
 *
 * @author zhonghua.wu
 * @create 2018-07-09 14:35
 */
public class SubDeviceLoginResponse extends BaseMqttResponse {
    private static final long serialVersionUID = -6444027073306760902L;
    private static Pattern pattern = Pattern.compile(ArrivedTopicPattern.SUB_DEVICE_LOGIN_REPLY);

    public String getSubProductKey() {
        Map<String, String> data = getData();
        return data == null ? null : data.get("productKey");
    }

    public String getSubDeviceKey() {
        Map<String, String> data = getData();
        return data == null ? null : data.get("deviceKey");
    }

    public String getSubDeviceSecret(){
        Map<String, String> data = getData();
        return data == null ? null : data.get("deviceSecret");
    }

    public String getSubDevceAssetId(){
        Map<String, String> data = getData();
        return data == null ? null : data.get("assetId");
    }

    @Override
    public Pattern getMatchTopicPattern() {
        return pattern;
    }
}
