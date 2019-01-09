package com.envisioniot.enos.iot_mqtt_sdk.message.downstream.activate;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.DeliveryTopicFormat;
import com.envisioniot.enos.iot_mqtt_sdk.message.downstream.BaseMqttReply;

/**
 * @author zhensheng.cai
 * @date 2019/1/8.
 */
public class DeviceActivateInfoReply extends BaseMqttReply {

    private static final long serialVersionUID = -6043579674292478615L;

    public static Builder builer(){
        return new Builder();
    }

    public static class Builder extends BaseMqttReply.Builder<Builder, DeviceActivateInfoReply>{

        @Override
        protected Object createData() {
            return null;
        }

        @Override
        protected DeviceActivateInfoReply createRequestInstance() {
            return new DeviceActivateInfoReply();
        }

    }

    @Override
    protected String _getPK_DK_FormatTopic() {
        return DeliveryTopicFormat.ACTIVATE_INFO_REPLY;
    }
}
