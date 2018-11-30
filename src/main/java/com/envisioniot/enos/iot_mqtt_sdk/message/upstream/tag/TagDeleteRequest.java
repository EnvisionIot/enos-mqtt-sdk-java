package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.tag;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.DeliveryTopicFormat;
import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.MethodConstants;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.BaseMqttRequest;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.*;

/**
 * Description: delete device tags
 * params:[
 *  {
 *      "tagKey":key
 *  },{
 *      "tagKey":key2
 *  }
 * ]
 *
 * ====>
 * "params": [
 "tags": ["tag1", "tag2"]
 * ],
 * @author zhonghua.wu
 * @create 2018-07-09 14:43
 */
public class TagDeleteRequest extends BaseMqttRequest<TagDeleteResponse> {
    private static final long serialVersionUID = 8471360794763108486L;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends BaseMqttRequest.Builder<Builder, TagDeleteRequest> {
        private Set<String> tags = new HashSet<>();

        @Override
        protected String createMethod() {
            return MethodConstants.TAG_DELETE;
        }

        @Override
        protected Object createParams() {
            return ImmutableMap.of("tags", new ArrayList<>(tags));
        }

        public Builder addTagKey(String tagKey) {
            tags.add(tagKey);
            return this;
        }

        public Builder addTagKeys(Collection<String> tagKeys) {
            tags.addAll(tagKeys);
            return this;
        }

        public Builder setTags(Collection<String> tags) {
            this.tags = new HashSet<>(tags);
            return this;
        }


        @Override
        protected TagDeleteRequest createRequestInstance() {
            return new TagDeleteRequest();
        }
    }


    private TagDeleteRequest() {
    }

    @Override
    public Class<TagDeleteResponse> getAnswerType() {
        return TagDeleteResponse.class;
    }

    @Override
    protected String _getPK_DK_FormatTopic() {
        return DeliveryTopicFormat.TAG_DELETE_TOPIC_FMT;
    }
}
