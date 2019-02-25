package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.ota;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.DeliveryTopicFormat;
import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.MethodConstants;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.BaseMqttRequest;
import com.google.common.collect.Maps;

import java.util.Map;

public class OtaVersionReportRequest extends BaseMqttRequest<OtaVersionReportResponse> {
    private static final long serialVersionUID = 234410161857671987L;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends BaseMqttRequest.Builder<Builder, OtaVersionReportRequest> {
        private String version;

        public Builder setVersion(String version) {
            this.version = version;
            return this;
        }

        @Override
        protected String createMethod() {
            return MethodConstants.OTA_INFORM;
        }

        @Override
        protected Object createParams() {
            Map<String, String> map = Maps.newHashMap();
            map.put("version", version);
            return map;
        }

        @Override
        protected OtaVersionReportRequest createRequestInstance() {
            return new OtaVersionReportRequest();
        }
    }

    @Override
    public Class<OtaVersionReportResponse> getAnswerType() {
        return OtaVersionReportResponse.class;
    }

    @Override
    protected String _getPK_DK_FormatTopic() {
        return DeliveryTopicFormat.VERSION_REPORT_TOPIC_FMT;
    }

    private OtaVersionReportRequest() {
    }
}
