package com.envisioniot.enos.iot_mqtt_sdk.message.downstream.activate;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.ArrivedTopicPattern;
import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.FieldConstants;
import com.envisioniot.enos.iot_mqtt_sdk.message.downstream.BaseMqttCommand;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.register.DeviceBasicInfo;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author zhensheng.cai
 * @date 2019/1/3.
 */
public class DeviceActivateInfoCommand extends BaseMqttCommand<DeviceActivateInfoReply> {

    private static final long serialVersionUID = -3428537655238537587L;
    private static Pattern pattern = Pattern.compile(ArrivedTopicPattern.ACTIVATE_INFO);

    public DeviceBasicInfo getDeviceInfo() {
        DeviceBasicInfo deviceInfo = new DeviceBasicInfo();
        Map<String, String> data = this.getParams();
        deviceInfo.productKey = data.get(FieldConstants.PRODUCT_KEY);
        deviceInfo.deviceKey = data.get(FieldConstants.DEVICE_KEY);
        deviceInfo.deviceSecret = data.get(FieldConstants.DEVICE_SECRET);
        deviceInfo.assetId = data.get(FieldConstants.ASSET_ID);
        return deviceInfo;
    }


    @Override
    public Pattern getMatchTopicPattern() {
        return pattern;
    }

    @Override
    public Class<DeviceActivateInfoReply> getAnswerType() {
        return DeviceActivateInfoReply.class;
    }
}
