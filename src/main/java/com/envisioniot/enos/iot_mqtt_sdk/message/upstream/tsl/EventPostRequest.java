package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.tsl;

import com.envisioniot.enos.iot_mqtt_sdk.core.exception.EnvisionException;
import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.DeliveryTopicFormat;
import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.MethodConstants;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.BaseMqttRequest;
import com.envisioniot.enos.iot_mqtt_sdk.util.CheckUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * { "id": "123", "version": "1.0", "params": { "value": { "Power": "on", "WF":
 * "2" }, "time": 1524448722000 }, "method":
 * "thing.event.{tsl.event.identifier}.post" }
 * --------
 * <p>
 * "params":{
 * "events":{
 * "Power":{
 * "value":"1.0,
 * "quality": "9"
 * },
 * "temp":1.02 ,
 * "branchCurr":[
 * "1.02","2.02","7.93"
 * ]
 * }
 * "time":123456 //optional
 * }
 *
 * @author zhensheng.cai
 * @date 2018/7/3.
 */
public class EventPostRequest extends BaseMqttRequest<EventPostResponse> {
    private static final long serialVersionUID = -8186172184432202539L;
    private String eventIdentifier;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends BaseMqttRequest.Builder<Builder, EventPostRequest> {
        private String eventIdentifier;
        private Map<String, Object> params = new HashMap<>();

        public Builder() {
            params.put("events", new HashMap<String, Object>());
            params.put("time", System.currentTimeMillis());
        }

        public Builder setEventIdentifier(String eventIdentifier) {
            this.eventIdentifier = eventIdentifier;
            return this;
        }

        @SuppressWarnings("unchecked")
        public Builder addValue(String point, Object value) {
            Map<String, Object> values = (Map<String, Object>) params.get("events");
            values.put(point, value);
            return this;
        }


        @SuppressWarnings("unchecked")
        public Builder addValues(Map<String, Object> value) {
            Map<String, Object> values = (Map<String, Object>) params.get("events");
            values.putAll(value);
            return this;
        }

        @SuppressWarnings("unchecked")
        public Builder setValues(Map<String, Object> value) {
            params.put("events", value);
            return this;
        }

        public Builder setTimestamp(long timestamp) {
            params.put("time", timestamp);
            return this;
        }

        @Override
        protected String createMethod() {
            return String.format(MethodConstants.EVENT_POST, this.eventIdentifier);
        }

        @Override
        protected Object createParams() {
            return params;
        }

        @Override
        protected EventPostRequest createRequestInstance() {
            return new EventPostRequest(eventIdentifier);
        }
    }


    private EventPostRequest(String eventIdentifier) {
        super();
        this.eventIdentifier = eventIdentifier;
    }

    @Override
    public void check() throws EnvisionException {
        super.check();
        CheckUtil.checkNotEmpty(eventIdentifier, "event.identifier");
    }

    @Override
    public String getMessageTopic() {
        return String.format(_getPK_DK_FormatTopic(), getProductKey(), getDeviceKey(), eventIdentifier);
    }

    @Override
    public Class<EventPostResponse> getAnswerType() {
        return EventPostResponse.class;
    }

    @Override
    public int getQos() {
        return 0;
    }

    @Override
    protected String _getPK_DK_FormatTopic() {
        return DeliveryTopicFormat.EVENT_POST;
    }


}
