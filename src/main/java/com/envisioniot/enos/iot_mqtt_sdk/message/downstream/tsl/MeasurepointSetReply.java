package com.envisioniot.enos.iot_mqtt_sdk.message.downstream.tsl;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.DeliveryTopicFormat;
import com.envisioniot.enos.iot_mqtt_sdk.message.downstream.BaseMqttReply;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.ResponseCode;

/**
 * @author zhensheng.cai
 * @date 2018/7/10.
 */
public class MeasurepointSetReply extends BaseMqttReply
{

	public MeasurepointSetReply(String productKey, String deviceKey)
	{
		setProductKey(productKey);
		setDeviceKey(deviceKey);
		this.setCode(ResponseCode.SUCCESS);
		this.setData("");
	}

    @Override
    protected String _getPK_DK_FormatTopic()
    {
        return DeliveryTopicFormat.MEASUREPOINT_SET_REPLY;
    }
}
