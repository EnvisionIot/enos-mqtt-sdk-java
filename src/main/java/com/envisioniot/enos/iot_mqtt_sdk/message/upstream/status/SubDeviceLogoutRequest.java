package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.status;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.DeliveryTopicFormat;
import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.MethodConstants;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.BaseMqttRequest;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Description: sub-device login request
 *
 * @author zhonghua.wu
 * @create 2018-07-09 14:39
 */
public class SubDeviceLogoutRequest extends BaseMqttRequest<SubDeviceLogoutResponse> {

    private static final long serialVersionUID = -3710208417177110404L;


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends BaseMqttRequest.Builder<Builder, SubDeviceLogoutRequest> {
        private String subProductKey;
        private String subDeviceKey;

        public Builder setSubProductKey(String subProductKey) {
            this.subProductKey = subProductKey;
            return this;
        }

        public Builder setSubDeviceKey(String subDeviceKey) {
            this.subDeviceKey = subDeviceKey;
            return this;
        }

        @Override
        protected String createMethod() {
            return MethodConstants.SUB_DEVICE_LOGOUT;
        }

        @Override
        protected Object createParams() {
            Map<String, String> params = Maps.newHashMap();
            params.put("productKey", subProductKey);
            params.put("deviceKey", subDeviceKey);
            return params;
        }

        @Override
        protected SubDeviceLogoutRequest createRequestInstance() {
            return new SubDeviceLogoutRequest();
        }
    }

    private SubDeviceLogoutRequest() {
    }

    @Override
    public Class<SubDeviceLogoutResponse> getAnswerType() {
        return SubDeviceLogoutResponse.class;
    }

    @Override
    protected String _getPK_DK_FormatTopic() {
        return DeliveryTopicFormat.SUB_DEVICE_LOGOUT_TOPIC_FMT;
    }
}
