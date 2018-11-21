package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.tag;

import com.envisioniot.enos.iot_mqtt_sdk.core.exception.EnvisionException;
import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.DeliveryTopicFormat;
import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.MethodConstants;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.BaseMqttRequest;
import com.envisioniot.enos.iot_mqtt_sdk.util.CheckUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * Description: update device tags
 * {
 *     "params":[
 *      {
 *          "tagKey":"key",
 *          "tagValue":"value"
 *      }
 *     ]
 * }
 * @author zhensheng.cai
 */
public class TagUpdateRequest extends BaseMqttRequest<TagUpdateResponse> {
    private static final long serialVersionUID = 8472140661039105628L;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends BaseMqttRequest.Builder<Builder, TagUpdateRequest> {
        private Map<String, String> tags = Maps.newHashMap();

        public Builder addTag(String tagKey, String tagValue) {
            tags.put(tagKey, tagValue);
            return this;
        }

        public Builder addTags(Map<String, String> tags) {
            this.tags.putAll(tags);
            return this;
        }

        @Override
        protected String createMethod() {
            return MethodConstants.TAG_UPDATE;
        }

        @Override
        protected Object createParams() {
            List<Map<String, String>> params = Lists.newArrayList();
            for (Map.Entry<String, String> entry : tags.entrySet()) {
                Map<String, String> map = Maps.newHashMap();
                map.put("tagKey", entry.getKey());
                map.put("tagValue", entry.getValue());
                params.add(map);
            }
            return params;
        }

        @Override
        protected TagUpdateRequest createRequestInstance() {
            return new TagUpdateRequest();
        }
    }

    private TagUpdateRequest() {
    }


    @Override
    public Class<TagUpdateResponse> getAnswerType() {
        return TagUpdateResponse.class;
    }

    @Override
    public void check() throws EnvisionException {
        super.check();
        List<Map<String, String>> params = getParams();
        for (Map<String, String> param : params) {
            CheckUtil.checkNotEmpty(param.get("tagKey"), "tagKey");
            CheckUtil.checkNotEmpty(param.get("tagValue"), "tagValue");
        }
    }

    @Override
    protected String _getPK_DK_FormatTopic() {
        return DeliveryTopicFormat.TAG_UPDATE_TOPIC_FMT;
    }

}
