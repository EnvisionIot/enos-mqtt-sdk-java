package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.ota;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.DeliveryTopicFormat;
import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.MethodConstants;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.BaseMqttRequest;
import com.google.common.collect.Maps;

import java.util.Map;

public class OtaGetVersionRequest extends BaseMqttRequest<OtaGetVersionResponse> {
    private static final long serialVersionUID = 1342670380317469907L;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends BaseMqttRequest.Builder<Builder, OtaGetVersionRequest> {

        @Override
        protected String createMethod() {
            return MethodConstants.OTA_GETVERSION;
        }

        @Override
        protected Object createParams() {
            Map<String, String> map = Maps.newHashMap();
            return map;
        }

        @Override
        protected OtaGetVersionRequest createRequestInstance() {
            return new OtaGetVersionRequest();
        }
    }

    @Override
    public Class<OtaGetVersionResponse> getAnswerType() {
        return OtaGetVersionResponse.class;
    }

    @Override
    protected String _getPK_DK_FormatTopic() {
        return DeliveryTopicFormat.GET_VERSION_TOPIC_FMT;
    }

    private OtaGetVersionRequest() {
    }
}
