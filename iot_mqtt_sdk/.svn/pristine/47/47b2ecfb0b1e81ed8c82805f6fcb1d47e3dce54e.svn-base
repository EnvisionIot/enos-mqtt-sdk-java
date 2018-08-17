package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.topo;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.ArrivedTopicPattern;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.BaseMqttResponse;
import com.envisioniot.enos.iot_mqtt_sdk.util.Pair;
import com.google.common.collect.Lists;

/**
 * Description: get topotaxy response
 *
 * @author zhonghua.wu
 * @create 2018-07-09 14:26
 */
public class GetTopoResponse extends BaseMqttResponse
{

	private static Pattern pattern = Pattern.compile(ArrivedTopicPattern.TOPO_GET_REPLY);

	public List<Pair<String, String>> getSubDeviceInfo()
	{
		List<Pair<String, String>> subDeviceInfo = Lists.newArrayList();
		List<Map<String, String>> data = ((List<Map<String, String>>) getData());
		for (Map<String, String> map : data)
		{
			subDeviceInfo.add(Pair.makePair(map.get("productKey"), map.get("deviceKey")));
		}
		return subDeviceInfo;
	}

    @Override
    public Pattern getMatchTopicPattern()
    {
		return pattern;
	}
}
