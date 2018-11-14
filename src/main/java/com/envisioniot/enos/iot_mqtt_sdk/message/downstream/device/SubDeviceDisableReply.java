package com.envisioniot.enos.iot_mqtt_sdk.message.downstream.device;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.DeliveryTopicFormat;
import com.envisioniot.enos.iot_mqtt_sdk.message.downstream.BaseMqttReply;

/**
 * @author zhensheng.cai
 * @date 2018/11/12.
 */
public class SubDeviceDisableReply extends BaseMqttReply {
    private static final long serialVersionUID = 3813658991372180907L;


    public static Builder builder(){
        return new Builder();
    }
    public static class Builder extends BaseMqttReply.Builder<Builder, SubDeviceDisableReply>{

        private Object data;
        public Builder() {
            this.data = "";
        }

        public Builder setData(Object data) {
            this.data = data;
            return this;
        }

        @Override
        protected Object createData() {
            return data;
        }

        @Override
        protected SubDeviceDisableReply createRequestInstance() {
            return new SubDeviceDisableReply();
        }
    }


    @Override
    protected String _getPK_DK_FormatTopic() {
        return DeliveryTopicFormat.SUB_DEVICE_DISABLE_REPLY;
    }
}
