package com.envisioniot.enos.iot_mqtt_sdk.message.downstream.device;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.DeliveryTopicFormat;
import com.envisioniot.enos.iot_mqtt_sdk.message.downstream.BaseMqttReply;

/**
 * Created by yi.dai on 2018/7/17.
 */
public class DisableDeviceCommandReply extends BaseMqttReply {

    private static final long serialVersionUID = 8783645772037686254L;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends BaseMqttReply.Builder<Builder, DisableDeviceCommandReply> {

        @Override
        protected Object createData() {
            return "";
        }

        @Override
        protected DisableDeviceCommandReply createRequestInstance() {
            return new DisableDeviceCommandReply();
        }
    }

    @Override
    protected String _getPK_DK_FormatTopic() {
        return DeliveryTopicFormat.DISABLE_DEVICE_REPLY;
    }


}
