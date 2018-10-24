package com.envisioniot.enos.iot_mqtt_sdk.message.downstream.tsl;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.DeliveryTopicFormat;
import com.envisioniot.enos.iot_mqtt_sdk.message.downstream.BaseMqttReply;

/**
 * @author zhensheng.cai
 * @date 2018/7/12.
 */
public class ModelDownRawReply extends BaseMqttReply
{

	private static final long serialVersionUID = 3238459840562301410L;

	private byte[] payload;

	public ModelDownRawReply() {}

	public ModelDownRawReply(byte[] payload)
	{
		this.payload = payload;
	}

	public void setPayload(byte[] payload)
	{
		this.payload = payload;
	}

	public byte[] getPayload()
	{
		return payload;
	}

	@Override
	public byte[] encode()
	{
		return payload;
	}

	@Override
    protected String _getPK_DK_FormatTopic()
    {
        return DeliveryTopicFormat.MODEL_DOWN_RAW_REPLY;
    }

	
}
