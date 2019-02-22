package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.tsl;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.DeliveryTopicFormat;
import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.MethodConstants;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.BaseMqttRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhensheng.cai
 * @date 2019/1/23.
 */
public class MeasurepointPostBatchRequest extends BaseMqttRequest<MeasurepointPostBatchResponse>{


    private static final long serialVersionUID = -4792098821956005525L;


    public static Builder builder(){
        return new Builder();
    }

    public static class Builder extends BaseMqttRequest.Builder<Builder, MeasurepointPostBatchRequest>{


        List<MeasurepointPostRequest> requests = new ArrayList<>();


        public Builder setRequests(List<MeasurepointPostRequest> requests) {
            this.requests = requests;
            return this;
        }

        public Builder addRequests(List<MeasurepointPostRequest> requests) {
            this.requests.addAll(requests);
            return this;
        }

        public Builder addRequest(MeasurepointPostRequest request){
            this.requests.add(request);
            return this;
        }

        @Override
        protected String createMethod() {
            return MethodConstants.MEASUREPOINT_POST_BATCH;
        }

        @Override
        protected Object createParams() {
            List<Map<String, Object>> params = new ArrayList<>();
            for (MeasurepointPostRequest request : requests) {
                Map<String, Object> param = new HashMap<>();
                if(request.getProductKey() !=null ){
                    param.put("productKey", request.getProductKey());
                }
                if(request.getDeviceKey() !=null ){
                    param.put("deviceKey", request.getDeviceKey());
                }
                Map<String, Object> map = request.getParams();
                if (map.containsKey("measurepoints")) {
                    param.put("measurepoints", map.get("measurepoints"));
                }
                if(map.containsKey("time")){
                    param.put("time", map.get("time"));
                }
                else{
                    param.put("time", System.currentTimeMillis());
                }
                params.add(param);
            }
            return params;
        }

        @Override
        protected MeasurepointPostBatchRequest createRequestInstance() {
            return new MeasurepointPostBatchRequest();
        }

    }


    @Override
    public Class<MeasurepointPostBatchResponse> getAnswerType() {
        return MeasurepointPostBatchResponse.class;
    }

    @Override
    protected String _getPK_DK_FormatTopic() {
        return DeliveryTopicFormat.MEASUREPOINT_POST_BATCH;
    }
}
