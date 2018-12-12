package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.topo;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.DeliveryTopicFormat;
import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.MethodConstants;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.BaseMqttRequest;
import com.envisioniot.enos.iot_mqtt_sdk.util.Pair;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * Description: delete topotaxy request
 * {
 * "params":[{
 * "productKey" :"xxxx",
 * "deviceKey" :"yyyy"
 * },{
 * "productKey" :"xxxx",
 * "deviceKey" :"yyyy"
 * }
 * ]
 * }
 *
 * @author zhonghua.wu
 * @create 2018-07-09 14:28
 */
public class TopoDeleteRequest extends BaseMqttRequest<TopoDeleteResponse> {
    private static final long serialVersionUID = 5902714018227720689L;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends BaseMqttRequest.Builder<Builder, TopoDeleteRequest> {
        private List<Pair<String, String>> subDeviceList = Lists.newArrayList();

        public Builder setSubDevices(List<Pair<String, String>> subDeviceList) {
            this.subDeviceList = subDeviceList;
            return this;
        }

        public Builder setSubDeviceList(List<Pair<String, String>> subDeviceList) {
            this.subDeviceList = subDeviceList;
            return this;
        }

        public Builder addSubDevice(String productKey, String deviceKey) {
            subDeviceList.add(Pair.makePair(productKey, deviceKey));
            return this;
        }

        public Builder addSubDevices(List<Pair<String, String>> subDeviceList) {
            this.subDeviceList.addAll(subDeviceList);
            return this;
        }

        @Override
        protected String createMethod() {
            return MethodConstants.TOPO_DELETE;
        }

        @Override
        protected Object createParams() {
            List<Map<String, String>> params = Lists.newArrayList();
            for (Pair<String, String> pair : subDeviceList) {
                Map<String, String> map = Maps.newHashMap();
                map.put("productKey", pair.getFirst());
                map.put("deviceKey", pair.getSecond());
                params.add(map);
            }
            return params;
        }

        @Override
        protected TopoDeleteRequest createRequestInstance() {
            return new TopoDeleteRequest();
        }
    }

    private TopoDeleteRequest() {
    }

    @Override
    public Class<TopoDeleteResponse> getAnswerType() {
        return TopoDeleteResponse.class;
    }

    @Override
    protected String _getPK_DK_FormatTopic() {
        return DeliveryTopicFormat.TOPO_DELETE_TOPIC_FMT;
    }

}
