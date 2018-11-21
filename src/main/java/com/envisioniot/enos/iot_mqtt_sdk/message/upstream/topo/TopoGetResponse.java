package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.topo;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.ArrivedTopicPattern;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.BaseMqttResponse;
import com.envisioniot.enos.iot_mqtt_sdk.util.Pair;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Description: get topotaxy response
 * message :
 * <p>
 * {
 * "id": "123",
 * "code": 200,
 * "data": [
 * {
 * "deviceName": "deviceName1234",
 * "productKey": "1234556554"
 * }
 * ]
 * }
 *
 * @author zhonghua.wu
 * @create 2018-07-09 14:26
 */
public class TopoGetResponse extends BaseMqttResponse {

    private static final long serialVersionUID = -1454569981740206606L;
    private static Pattern pattern = Pattern.compile(ArrivedTopicPattern.TOPO_GET_REPLY);

    public List<Pair<String, String>> getSubDeviceInfo() {
        List<Pair<String, String>> subDeviceInfo = Lists.newArrayList();
        List<Map<String, String>> data = getData();
        for (Map<String, String> map : data) {
            subDeviceInfo.add(Pair.makePair(map.get("productKey"), map.get("deviceKey")));
        }
        return subDeviceInfo;
    }

    @Override
    public Pattern getMatchTopicPattern() {
        return pattern;
    }
}
