package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.tsl;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.DeliveryTopicFormat;
import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.MethodConstants;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.BaseMqttRequest;

import java.util.*;

/**
 * @author zhensheng.cai
 * @date 2018/11/21.
 */
public class AttributeDeleteRequest extends BaseMqttRequest<AttributeDeleteResponse> {

    private static final long serialVersionUID = -5215445403149087915L;

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder extends BaseMqttRequest.Builder<Builder,AttributeDeleteRequest>{

        private Set<String> attributes;

        public Builder() {
            this.attributes = new HashSet<>();
        }

        public AttributeDeleteRequest.Builder deleteAttribute(String key){
            this.attributes.add(key);
            return this;
        }

        public AttributeDeleteRequest.Builder deleteAttributes(Collection<String> key){
            this.attributes.addAll(key);
            return this;
        }


        @Override
        protected String createMethod() {
            return MethodConstants.ATTRIBUTE_DELETE;
        }

        @Override
        protected Object createParams() {
            Map<String, Object> params = new HashMap<>();
            params.put("attributes", new ArrayList<>(attributes));
            return params;
        }

        @Override
        protected AttributeDeleteRequest createRequestInstance() {
            return new AttributeDeleteRequest();
        }
    }


    private AttributeDeleteRequest() {
    }


    @Override

    public Class<AttributeDeleteResponse> getAnswerType() {
        return AttributeDeleteResponse.class;
    }

    @Override
    protected String _getPK_DK_FormatTopic() {
        return DeliveryTopicFormat.ATTRIBUTE_DELETE;
    }
}
