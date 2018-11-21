package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.tsl;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.DeliveryTopicFormat;
import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.MethodConstants;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.BaseMqttRequest;

import java.util.*;

/**
 * params:{
 *     "attributes":["attr1", "attr2"]
 * }
 *
 *
 *
 * @author zhensheng.cai
 * @date 2018/11/12.
 */
public class AttributeQueryRequest extends BaseMqttRequest<AttributeQueryResponse> {


    private static final long serialVersionUID = -4076276085564005759L;

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder extends BaseMqttRequest.Builder<Builder, AttributeQueryRequest>{
        private Set<String> attributes;

        public Builder() {
            this.attributes = new HashSet<>();
        }

        public Builder addAttribute(String key){
            this.attributes.add(key);
            return this;
        }

        public Builder addAttributes(Collection<String> key){
            this.attributes.addAll(key);
            return this;
        }

        public Builder setAttributes(Collection<String> key){
            this.attributes = new HashSet<>(key);
            return this;
        }

        public Builder queryAll(){
            this.attributes.clear();
            return this;
        }

        @Override
        protected String createMethod() {
            return MethodConstants.ATTRIBUTE_QUERY;
        }

        @Override
        protected Object createParams() {
            Map<String, Object> params = new HashMap<>();
            params.put("attributes", new ArrayList<>(attributes));
            return params;
        }

        @Override
        protected AttributeQueryRequest createRequestInstance() {
            return new AttributeQueryRequest();
        }

    }

    private AttributeQueryRequest() {
    }

    @Override
    public Class<AttributeQueryResponse> getAnswerType() {
        return AttributeQueryResponse.class;
    }

    @Override
    protected String _getPK_DK_FormatTopic() {
        return DeliveryTopicFormat.ATTRIBUTE_QUERY;
    }
}
