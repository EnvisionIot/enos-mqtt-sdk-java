package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.tag;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.DeliveryTopicFormat;
import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.MethodConstants;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.BaseMqttRequest;
import com.google.common.collect.ImmutableMap;

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
 @update 2018年11月21日
 ====>
 "params": [
 "tags": ["tag1", "tag2"]
 ],


 * @author zhensheng.cai
 * @date 2018/11/12.
 */
public class TagQueryRequest extends BaseMqttRequest<TagQueryResponse>{


    private static final long serialVersionUID = 5137153537827796184L;

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder extends BaseMqttRequest.Builder<Builder, TagQueryRequest>{

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
            return ImmutableMap.of("tags", new ArrayList<>(tagKeys));
        }

        @Override
        protected TagQueryRequest createRequestInstance() {
            return new TagQueryRequest();
        }

    }

    @Override
    public Class<TagQueryResponse> getAnswerType() {
        return TagQueryResponse.class;
    }

    @Override
    protected String _getPK_DK_FormatTopic() {
        return DeliveryTopicFormat.TAG_QUERY;
    }

}
