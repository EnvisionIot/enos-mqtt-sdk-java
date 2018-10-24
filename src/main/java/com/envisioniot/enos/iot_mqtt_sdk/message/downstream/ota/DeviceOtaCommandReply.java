package com.envisioniot.enos.iot_mqtt_sdk.message.downstream.ota;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.DeliveryTopicFormat;
import com.envisioniot.enos.iot_mqtt_sdk.message.downstream.BaseMqttReply;

public class DeviceOtaCommandReply extends BaseMqttReply {

    private static final long serialVersionUID = 3221664283192647376L;

    @Override
    protected String _getPK_DK_FormatTopic() {
        return DeliveryTopicFormat.DEVICE_OTA_REPLY;
    }

    public static class Builder extends BaseMqttReply.Builder<Builder, DeviceOtaCommandReply> {

        @Override
        protected Object createData() {
            return "";
        }

        @Override
        protected DeviceOtaCommandReply createRequestInstance() {
            return new DeviceOtaCommandReply();
        }
    }
}
