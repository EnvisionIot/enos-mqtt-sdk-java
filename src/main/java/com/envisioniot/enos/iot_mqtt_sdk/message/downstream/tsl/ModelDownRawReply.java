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

	public static Builder builder(){
		return new Builder();
	}

	public static class Builder extends BaseMqttReply.Builder<Builder, ModelDownRawReply>{

		@Override protected Object createData()
		{
			return "";
		}

		@Override protected ModelDownRawReply createRequestInstance()
		{
			return new ModelDownRawReply();
		}
	}

	@Override
    protected String _getPK_DK_FormatTopic()
    {
        return DeliveryTopicFormat.MODEL_DOWN_RAW_REPLY;
    }

	
}
