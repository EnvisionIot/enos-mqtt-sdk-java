package com.envisioniot.enos.iot_mqtt_sdk.message.downstream.device;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.DeliveryTopicFormat;
import com.envisioniot.enos.iot_mqtt_sdk.message.downstream.BaseMqttReply;

/**
 * @author zhensheng.cai
 * @date 2018/11/12.
 */
public class SubDeviceDeleteReply extends BaseMqttReply{
    private static final long serialVersionUID = 862333620832074276L;

    public static Builder builder(){
        return new Builder();
    }
    public static class Builder extends BaseMqttReply.Builder<Builder, SubDeviceDeleteReply>{

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
        protected SubDeviceDeleteReply createRequestInstance() {
            return new SubDeviceDeleteReply();
        }
    }

    @Override
    protected String _getPK_DK_FormatTopic() {
        return DeliveryTopicFormat.SUB_DEVICE_DELETE_REPLY;
    }
}
