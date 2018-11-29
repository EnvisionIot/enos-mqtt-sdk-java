package com.envisioniot.enos.iot_mqtt_sdk.message.downstream.tsl;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.DeliveryTopicFormat;
import com.envisioniot.enos.iot_mqtt_sdk.message.downstream.BaseMqttReply;

/**
 * @author zhensheng.cai
 * @date 2018/7/10.
 */
public class MeasurepointSetReply extends BaseMqttReply {

    private static final long serialVersionUID = 7250499387416208335L;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends BaseMqttReply.Builder<Builder, MeasurepointSetReply> {

        @Override
        protected Object createData() {
            return null;
        }

        @Override
        protected MeasurepointSetReply createRequestInstance() {
            return new MeasurepointSetReply();
        }
    }


    @Override
    protected String _getPK_DK_FormatTopic() {
        return DeliveryTopicFormat.MEASUREPOINT_SET_REPLY;
    }
}
