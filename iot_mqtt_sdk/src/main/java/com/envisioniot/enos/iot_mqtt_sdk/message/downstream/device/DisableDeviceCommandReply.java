package com.envisioniot.enos.iot_mqtt_sdk.message.downstream.device;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.DeliveryTopicFormat;
import com.envisioniot.enos.iot_mqtt_sdk.message.downstream.BaseMqttReply;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.ResponseCode;

/**
 * Created by yi.dai on 2018/7/17.
 */
public class DisableDeviceCommandReply extends BaseMqttReply
{
	public DisableDeviceCommandReply(String productKey, String deviceKey)
	{
		setProductKey(productKey);
		setDeviceKey(deviceKey);
		this.setCode(ResponseCode.SUCCESS);
		this.setData("");
	}

    @Override
    protected String _getPK_DK_FormatTopic()
    {
        return DeliveryTopicFormat.DISABLE_DEVICE_REPLY;
    }


}
