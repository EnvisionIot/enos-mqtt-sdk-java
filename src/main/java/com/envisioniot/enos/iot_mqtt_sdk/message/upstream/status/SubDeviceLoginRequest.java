package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.status;

import com.envisioniot.enos.iot_mqtt_sdk.core.exception.EnvisionException;
import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.DeliveryTopicFormat;
import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.MethodConstants;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.BaseMqttRequest;
import com.envisioniot.enos.iot_mqtt_sdk.util.CheckUtil;

import java.util.Map;

/**
 * Description: sub-device login request
 *
 * @author zhonghua.wu
 * @date 2018-07-09 14:38
 */
public class SubDeviceLoginRequest extends BaseMqttRequest<SubDeviceLoginResponse> {

    private static final long serialVersionUID = -3347144897822328244L;

    private int secureMode = 2;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends BaseMqttRequest.Builder<Builder, SubDeviceLoginRequest> {
        private SubDeviceLoginInfo subDeviceInfo;
        private int securemode = 2;

        public Builder setSubDeviceInfo(String productKey, String deviceKey, String deviceSecret) {
            this.subDeviceInfo = new SubDeviceLoginInfo(productKey, deviceKey, deviceSecret);
            this.securemode = subDeviceInfo.getSecureMode();
            return this;
        }

        public Builder setSubDeviceInfo(String productKey, String productSecret , String deviceKey, String deviceSecret) {
            this.subDeviceInfo = new SubDeviceLoginInfo(productKey, productSecret, deviceKey, deviceSecret);
            this.securemode = subDeviceInfo.getSecureMode();
            return this;
        }


        @Override
        protected String createMethod() {
            return MethodConstants.SUB_DEVICE_LOGIN;
        }

        @Override
        protected Object createParams() {
            return this.subDeviceInfo.getSignParams();
        }


        @Override
        protected SubDeviceLoginRequest createRequestInstance() {
            SubDeviceLoginRequest request = new SubDeviceLoginRequest();
            request.secureMode = securemode;
            return request;
        }

    }

    private SubDeviceLoginRequest() {
    }

    private void setSecureMode(int secureMode){
        this.secureMode = secureMode;
    }

    public int getSecureMode(){
        return this.secureMode;
    }



    @Override
    public void check() throws EnvisionException {
        super.check();
        Map<String, String> params = getParams();
        CheckUtil.checkNotEmpty(params.get("productKey"), "subDeviceInfo.productKey");
        CheckUtil.checkNotEmpty(params.get("deviceKey"), "subDeviceInfo.deviceKey");
        CheckUtil.checkNotEmpty(params.get("clientId"), "subDeviceInfo.client");
        CheckUtil.checkNotEmpty(params.get("signMethod"), "subDeviceInfo.signMethod");
        CheckUtil.checkNotEmpty(params.get("sign"), "subDeviceInfo.sign");
    }

    @Override
    public Class<SubDeviceLoginResponse> getAnswerType() {
        return SubDeviceLoginResponse.class;
    }

    @Override
    protected String _getPK_DK_FormatTopic() {
        return DeliveryTopicFormat.SUB_DEVICE_LOGIN_TOPIC_FMT;
    }
}
