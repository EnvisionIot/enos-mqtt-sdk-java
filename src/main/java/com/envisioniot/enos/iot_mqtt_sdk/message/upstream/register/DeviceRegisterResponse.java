package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.register;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.ArrivedTopicPattern;
import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.FieldConstants;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.BaseMqttResponse;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Description:
 *
 * @author zhonghua.wu
 * @create 2018-07-09 14:09
 */
public class DeviceRegisterResponse extends BaseMqttResponse {


    private static final long serialVersionUID = -6750111299164581512L;
    private static Pattern pattern = Pattern.compile(ArrivedTopicPattern.SUB_DEVICE_REGISTER_REPLY);

    public List<DeviceBasicInfo> getDeviceBasicInfoList() {
        List<Map<String, String>> dataList = getData();
        List<DeviceBasicInfo> results = Lists.newArrayList();
        for (Map<String, String> data : dataList) {
            DeviceBasicInfo dbi = new DeviceBasicInfo();
            dbi.productKey = data.get(FieldConstants.PRODUCT_KEY);
            dbi.deviceKey = data.get(FieldConstants.DEVICE_KEY);
            dbi.deviceSecret = data.get(FieldConstants.DEVICE_SECRET);
            dbi.assetId = data.get(FieldConstants.ASSET_ID);
            results.add(dbi);
        }
        return results;
    }

    @Override
    public Pattern getMatchTopicPattern() {
        return pattern;
    }
}
