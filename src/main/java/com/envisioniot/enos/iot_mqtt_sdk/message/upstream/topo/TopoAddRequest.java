package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.topo;

import com.envisioniot.enos.iot_mqtt_sdk.core.exception.EnvisionException;
import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.DeliveryTopicFormat;
import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.MethodConstants;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.BaseMqttRequest;
import com.envisioniot.enos.iot_mqtt_sdk.util.CheckUtil;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

/**
 * Description: add topo request
 * {
 *     "params":[
 *      {
 *          "productKey":
 *          "deviceKey":
 *          "sign"
 *      }
 *
 *     ]
 * }
 * @author zhonghua.wu
 * @create 2018-07-04 11:12
 */
public class TopoAddRequest extends BaseMqttRequest<TopoAddResponse> {
    private static final long serialVersionUID = -5999452330092417149L;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends BaseMqttRequest.Builder<Builder, TopoAddRequest> {
        private List<SubDeviceInfo> subDeviceInfoList = Lists.newArrayList();

        public Builder setSubDeviceInfoList(List<SubDeviceInfo> subDeviceInfoList) {
            this.subDeviceInfoList = subDeviceInfoList;
            return this;
        }

        public Builder addSubDevice(String subProductKey, String subDeviceKey, String subDeviceSecret) {
            subDeviceInfoList.add(new SubDeviceInfo(subProductKey, subDeviceKey, subDeviceSecret));
            return this;
        }

        public Builder addSubDevice(SubDeviceInfo deviceInfo) {
            subDeviceInfoList.add(deviceInfo);
            return this;
        }

        public Builder addSubDevices(List<SubDeviceInfo> deviceInfoList) {
            subDeviceInfoList.addAll(deviceInfoList);
            return this;
        }

        @Override
        protected String createMethod() {
            return MethodConstants.TOPO_ADD;
        }

        @Override
        protected Object createParams() {
            List<Map<String, String>> params = Lists.newArrayList();
            for (SubDeviceInfo deviceInfo : subDeviceInfoList) {
                params.add(deviceInfo.createSignMap());
            }
            return params;
        }

        @Override
        protected TopoAddRequest createRequestInstance() {
            return new TopoAddRequest();
        }
    }


    private TopoAddRequest() {
    }

    @Override
    public Class<TopoAddResponse> getAnswerType() {
        return TopoAddResponse.class;
    }

    @Override
    public void check() throws EnvisionException {
        super.check();
        List<Map<String, String>> params = getParams();
        CheckUtil.checkNotEmpty(params, "subDeviceInfoList");
        for (Map<String, String> param : params) {
            CheckUtil.checkNotEmpty(param.get("productKey"), "subDeviceInfo.productKey");
            CheckUtil.checkNotEmpty(param.get("deviceKey"), "subDeviceInfo.deviceKey");
            CheckUtil.checkNotEmpty(param.get("clientId"), "subDeviceInfo.client");
            CheckUtil.checkNotEmpty(param.get("signMethod"), "subDeviceInfo.signMethod");
            CheckUtil.checkNotEmpty(param.get("sign"), "subDeviceInfo.sign");
        }
    }

    @Override
    protected String _getPK_DK_FormatTopic() {
        return DeliveryTopicFormat.TOPO_ADD_TOPIC_FMT;
    }
}
