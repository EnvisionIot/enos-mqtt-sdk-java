package com.envisioniot.enos.iot_mqtt_sdk.message.downstream.tsl;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.DeliveryTopicFormat;
import com.envisioniot.enos.iot_mqtt_sdk.message.downstream.BaseMqttReply;

import java.util.HashMap;
import java.util.Map;

/**
 * { "id": "123", "code": 200, "data": { "power": "on", "temp": "23" } }
 *
 * @author zhensheng.cai
 * @date 2018/7/12.
 */

public class MeasurepointGetReply extends BaseMqttReply {

    private static final long serialVersionUID = -8407011589653796967L;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends BaseMqttReply.Builder<Builder, MeasurepointGetReply> {
        private Map<String, Object> data = new HashMap<String, Object>();

        public Builder addMeaurepoint(String pointKey, Object value) {
            data.put(pointKey, value);
            return this;
        }

        public Builder addMeasurepoints(Map<String, Object> points) {
            data.putAll(points);
            return this;
        }

        public Builder setMeasurepoints(Map<String, Object> points) {
            data = points;
            return this;
        }

        @Override
        protected Object createData() {
            return data;
        }

        @Override
        protected MeasurepointGetReply createRequestInstance() {
            return new MeasurepointGetReply();
        }

    }

    @Override
    protected String _getPK_DK_FormatTopic() {
        return DeliveryTopicFormat.MEASUREPOINT_GET_REPLY;
    }

}
