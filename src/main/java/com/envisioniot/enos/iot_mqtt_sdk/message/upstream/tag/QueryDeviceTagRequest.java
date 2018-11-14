package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.tag;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.DeliveryTopicFormat;
import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.MethodConstants;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.BaseMqttRequest;

import java.util.*;

/**
 * "params":[
 {
 "tagKey":key
 },{
 "tagKey":key2
 },{
 …
 }
 ],
 * @author zhensheng.cai
 * @date 2018/11/12.
 */
public class QueryDeviceTagRequest extends BaseMqttRequest<QueryDeviceTagResponse>{


    private static final long serialVersionUID = 5137153537827796184L;

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder extends BaseMqttRequest.Builder<Builder, QueryDeviceTagRequest>{

        private Set<String> tagKeys;
        public Builder() {
            tagKeys = new HashSet<>();
        }

        public Builder addKey(String key) {
            this.tagKeys.add(key);
            return this;
        }

        public Builder addKeys(Collection<String> key){
            this.tagKeys.addAll(key);
            return this;
        }

        public Builder setKeys(Collection<String> keys){
            this.tagKeys = new HashSet<>(keys);
            return this;
        }

        public Builder queryAll(){
            this.tagKeys = new HashSet<>();
            return this;
        }


        @Override
        protected String createMethod() {
            return MethodConstants.TAG_QUERY;
        }

        @Override
        protected Object createParams() {
            List<Map<String, String>> params = new ArrayList<>();
            tagKeys.forEach(tagkey->{
                Map<String, String> value = new HashMap<>();
                value.put("tagKey", tagkey);
                params.add(value);
            });
            return params;
        }

        @Override
        protected QueryDeviceTagRequest createRequestInstance() {
            return new QueryDeviceTagRequest();
        }

    }

    @Override
    public Class<QueryDeviceTagResponse> getAnswerType() {
        return QueryDeviceTagResponse.class;
    }

    @Override
    protected String _getPK_DK_FormatTopic() {
        return DeliveryTopicFormat.TAG_QUERY;
    }

}
