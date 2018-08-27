package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.tag;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.DeliveryTopicFormat;
import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.MethodConstants;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.BaseMqttRequest;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Description: delete device tags
 *
 * @author zhonghua.wu
 * @create 2018-07-09 14:43
 */
public class DeleteDeviceTagRequest extends BaseMqttRequest<DeleteDeviceTagResponse>
{
    private static final long serialVersionUID = 8471360794763108486L;

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder extends BaseMqttRequest.Builder<Builder,DeleteDeviceTagRequest>
    {
        private List<String> tags = Lists.newArrayList();

        @Override protected String createMethod()
        {
            return MethodConstants.TAG_DELETE;
        }

        @Override protected Object createParams()
        {
            List<Map<String, String>> params = Lists.newArrayList();
            for (String tagKey : tags)
            {
                Map<String, String> keyMap = Maps.newHashMap();
                keyMap.put("tagKey", tagKey);
                params.add(keyMap);
            }
            return params;
        }

        public Builder addTagKey(String tagKey)
        {
            tags.add(tagKey);
            return this;
        }

        public Builder addTagKeys(Collection<String> tagKeys)
        {
            tags.addAll(tagKeys);
            return this;
        }

        public Builder setTags(List<String> tags)
        {
            this.tags = tags;
            return this;
        }


        @Override protected DeleteDeviceTagRequest createRequestInstance()
        {
            return new DeleteDeviceTagRequest();
        }
    }


    private DeleteDeviceTagRequest(){
    }

    @Override
    public Class<DeleteDeviceTagResponse> getAnswerType()
    {
        return DeleteDeviceTagResponse.class;
    }

    @Override
    protected String _getPK_DK_FormatTopic()
    {
        return DeliveryTopicFormat.TAG_DELETE_TOPIC_FMT;
    }
}
