package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.tsl;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.DeliveryTopicFormat;
import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.MethodConstants;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.BaseMqttRequest;

import java.util.HashMap;
import java.util.Map;

/**
 *
 *  { "id": "123", "version": "1.0","params":
 *  , "method":thing.attribute.update" }
 * --------
 * <p>
 * "params":{
 * "attributes":{
 * "attr1":{
 * "value":"1.0,
 * "quality": "9"
 * },
 * "attr2":1.02 ,
 * "attr3":[
 * "1.02","2.02","7.93"
 * ]
 * }
 * }
 *
 * @author zhensheng.cai
 * @date 2018/11/9.
 */
public class AttributeUpdateRequest extends BaseMqttRequest<AttributeUpdateResponse>{

    private static final long serialVersionUID = -8790741282018130406L;

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder extends BaseMqttRequest.Builder<Builder, AttributeUpdateRequest>{

        public Map<String, Object> attributes;

        public Builder() {
            this.attributes = new HashMap<>();
        }

        public Builder addAttribute(String key, Object value) {
            this.attributes.put(key, value);
            return this;
        }

        public Builder addAttributes(Map<String ,Object> values){
            this.attributes.putAll(values);
            return this;
        }

        public Builder setAttributes(Map<String ,Object> values){
            this.attributes = values;
            return this;
        }

        @Override
        protected String createMethod() {
            return MethodConstants.ATTRIBUTE_UPDATE;
        }

        @Override
        protected Object createParams() {
            Map<String, Object> params = new HashMap<>();
            params.put("attributes", attributes);
            return params;
        }

        @Override
        protected AttributeUpdateRequest createRequestInstance() {
            return new AttributeUpdateRequest();
        }

    }


    @Override
    protected String _getPK_DK_FormatTopic() {
        return DeliveryTopicFormat.ATTRIBUTE_UPDATE;
    }

    @Override
    public Class<AttributeUpdateResponse> getAnswerType() {
        return null;
    }
}
