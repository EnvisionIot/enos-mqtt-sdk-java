package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.ota;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.DeliveryTopicFormat;
import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.MethodConstants;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.BaseMqttRequest;
import com.google.common.collect.Maps;

import java.util.Map;

public class VersionReportRequest extends BaseMqttRequest<VersionReportResponse> {


    /**
     *
     */
    private static final long serialVersionUID = 234410161857671987L;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends BaseMqttRequest.Builder<Builder, VersionReportRequest> {
        private String deviceId;
        private String firmwareId;

        public Builder setDeviceId(String iotId) {
            this.deviceId = iotId;
            return this;
        }

        public Builder setFirmwareId(String firmwareId) {
            this.firmwareId = firmwareId;
            return this;
        }

        @Override
        protected String createMethod() {
            return MethodConstants.PROGRESS_REPORT;
        }

        @Override
        protected Object createParams() {
            Map<String, String> map = Maps.newHashMap();
            map.put("deviceId", deviceId);
            map.put("firmwareId", firmwareId);
            return map;
        }

        @Override
        protected VersionReportRequest createRequestInstance() {
            return new VersionReportRequest();
        }
    }

    @Override
    public Class<VersionReportResponse> getAnswerType() {
        return VersionReportResponse.class;
    }

    @Override
    protected String _getPK_DK_FormatTopic() {
        return DeliveryTopicFormat.VERSION_REPORT_TOPIC_FMT;
    }

    private VersionReportRequest() {
    }
}
