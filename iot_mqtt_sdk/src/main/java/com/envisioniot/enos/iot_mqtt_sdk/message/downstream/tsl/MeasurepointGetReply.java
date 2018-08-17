package com.envisioniot.enos.iot_mqtt_sdk.message.downstream.tsl;

import java.util.HashMap;
import java.util.Map;

import com.envisioniot.enos.iot_mqtt_sdk.core.exception.EnvisionException;
import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.DeliveryTopicFormat;
import com.envisioniot.enos.iot_mqtt_sdk.message.downstream.BaseMqttReply;

/**
 * { "id": "123", "code": 200, "data": { "power": "on", "temp": "23" } }
 * 
 * 
 * @author zhensheng.cai
 * @date 2018/7/12.
 */

public class MeasurepointGetReply extends BaseMqttReply
{
	public MeasurepointGetReply(String productKey, String deviceKey)
	{
		setData(new HashMap<String, String>());
		setProductKey(productKey);
		setDeviceKey(deviceKey);
	}

	@SuppressWarnings("unchecked")
	public void addMeaurepoint(String pointKey, String value)
	{
		Map<String, String> data = (Map<String, String>) this.getData();
		data.put(pointKey, value);
	}

	@SuppressWarnings("unchecked")
	public void addMeasurepoint(String k1, String v1, String k2, String v2)
	{
		Map<String, String> data = (Map<String, String>) this.getData();
		data.put(k1, v1);
		data.put(k2, v2);
	}

	@SuppressWarnings("unchecked")
	public void addMeasurepoints(Map<String, String> points)
	{
		Map<String, String> data = (Map<String, String>) this.getData();
		data.putAll(points);
	}

    @Override
    protected String _getPK_DK_FormatTopic()
    {
        return DeliveryTopicFormat.MEASUREPOINT_GET_REPLY;
    }

}
