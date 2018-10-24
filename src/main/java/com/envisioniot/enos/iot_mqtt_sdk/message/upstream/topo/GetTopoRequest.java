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
public class GetTopoRequest extends BaseMqttRequest<GetTopoResponse> {
    private static final long serialVersionUID = 9043905757195657061L;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends BaseMqttRequest.Builder<Builder, GetTopoRequest> {
        @Override
        protected String createMethod() {
            return MethodConstants.TOPO_GET;
        }

        @Override
        protected Object createParams() {
            return Collections.emptyMap();
        }

        @Override
        protected GetTopoRequest createRequestInstance() {
            return new GetTopoRequest();
        }
    }

    private GetTopoRequest() {
    }

    @Override
    public Class<GetTopoResponse> getAnswerType() {
        return GetTopoResponse.class;
    }

    @Override
    protected String _getPK_DK_FormatTopic() {
        return DeliveryTopicFormat.TOPO_GET_TOPIC_FMT;
    }

}
