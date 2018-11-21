package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.topo;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.DeliveryTopicFormat;
import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.MethodConstants;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.BaseMqttRequest;

import java.util.Collections;

/**
 * Description: get topotaxy request
 *
 * @author zhonghua.wu
 * @create 2018-07-09 14:29
 */
public class TopoGetRequest extends BaseMqttRequest<TopoGetResponse> {
    private static final long serialVersionUID = 9043905757195657061L;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends BaseMqttRequest.Builder<Builder, TopoGetRequest> {
        @Override
        protected String createMethod() {
            return MethodConstants.TOPO_GET;
        }

        @Override
        protected Object createParams() {
            return Collections.emptyMap();
        }

        @Override
        protected TopoGetRequest createRequestInstance() {
            return new TopoGetRequest();
        }
    }

    private TopoGetRequest() {
    }

    @Override
    public Class<TopoGetResponse> getAnswerType() {
        return TopoGetResponse.class;
    }

    @Override
    protected String _getPK_DK_FormatTopic() {
        return DeliveryTopicFormat.TOPO_GET_TOPIC_FMT;
    }

}
