package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.ota;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.DeliveryTopicFormat;
import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.MethodConstants;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.BaseMqttRequest;
import com.google.common.collect.Maps;

import java.util.Map;

public class OtaProgressReportRequest extends BaseMqttRequest<OtaProgressReportResponse> {

    /**
     *
     */
    private static final long serialVersionUID = 1342670380317469907L;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends BaseMqttRequest.Builder<Builder, OtaProgressReportRequest> {
        private String deviceId;
        private int status;
        private String statusDesp;
        private String firmwareId;

        public Builder setDeviceId(String iotId) {
            this.deviceId = iotId;
            return this;
        }

        public Builder setStatus(int status) {
            this.status = status;
            return this;
        }

        public Builder setStatusDesp(String desp) {
            this.statusDesp = desp;
            return this;
        }

        public Builder setFirmwareId(String firmwareId) {
            this.firmwareId = firmwareId;
            return this;
        }

        @Override
        protected String createMethod() {
            return MethodConstants.OTA_PROGRESS;
        }

        @Override
        protected Object createParams() {
            Map<String, String> map = Maps.newHashMap();
            map.put("deviceId", deviceId);
            map.put("status", status + "");
            map.put("statusDesp", statusDesp);
            map.put("firmwareId", firmwareId);
            return map;
        }

        @Override
        protected OtaProgressReportRequest createRequestInstance() {
            return new OtaProgressReportRequest();
        }
    }

    @Override
    public Class<OtaProgressReportResponse> getAnswerType() {
        return OtaProgressReportResponse.class;
    }

    @Override
    protected String _getPK_DK_FormatTopic() {
        return DeliveryTopicFormat.PROGRESS_REPORT_TOPIC_FMT;
    }

    private OtaProgressReportRequest() {
    }
}
