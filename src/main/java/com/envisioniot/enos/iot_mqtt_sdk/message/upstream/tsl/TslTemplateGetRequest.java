package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.tsl;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.DeliveryTopicFormat;
import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.MethodConstants;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.BaseMqttRequest;

import java.util.Collections;

/**
 * {
 * "id": "123",
 * "version": "1.0",
 * "params": {},
 * "method": "thing.dsltemplate.get"
 * }
 *
 * @author zhensheng.cai
 * @date 2018/7/12.
 */
public class TslTemplateGetRequest extends BaseMqttRequest<TslTemplateGetResponse> {
    private static final long serialVersionUID = -8443886905071947074L;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends BaseMqttRequest.Builder<Builder, TslTemplateGetRequest> {
        @Override
        protected String createMethod() {
            return MethodConstants.TSL_TEMPLATE_GET;
        }

        @Override
        protected Object createParams() {
            return Collections.emptyMap();
        }

        @Override
        protected TslTemplateGetRequest createRequestInstance() {
            return new TslTemplateGetRequest();
        }

    }

    private TslTemplateGetRequest() {
    }

    @Override
    public Class<TslTemplateGetResponse> getAnswerType() {
        return TslTemplateGetResponse.class;
    }

    @Override
    protected String _getPK_DK_FormatTopic() {
        return DeliveryTopicFormat.TSL_TEMPLATE_GET;
    }

}
