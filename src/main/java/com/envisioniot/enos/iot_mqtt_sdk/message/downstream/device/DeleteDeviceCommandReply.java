package com.envisioniot.enos.iot_mqtt_sdk.message.downstream.device;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.DeliveryTopicFormat;
import com.envisioniot.enos.iot_mqtt_sdk.message.downstream.BaseMqttReply;

/**
 * Created by yi.dai on 2018/7/17.
 */
public class DeleteDeviceCommandReply extends BaseMqttReply
{

	private static final long serialVersionUID = -9194212758770027914L;

	public static Builder builder(){
		return new Builder();
	}

	public static class Builder extends BaseMqttReply.Builder<Builder, DeleteDeviceCommandReply>
	{

		@Override protected Object createData()
		{
			return "";
		}

		@Override protected DeleteDeviceCommandReply createRequestInstance()
		{
			return new DeleteDeviceCommandReply();
		}
	}

    @Override
    protected String _getPK_DK_FormatTopic()
    {
        return DeliveryTopicFormat.DELETE_DEVICE_REPLY;
    }

}
