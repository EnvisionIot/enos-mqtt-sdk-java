package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.tsl;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.DeliveryTopicFormat;
import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.MethodConstants;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.BaseMqttRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * { "id": "123", "version": "1.0", "params": { "Power": { "value": "on",
 * "time": 1524448722000 }, "WF": { "value": 23.6, "time": 1524448722000 } },
 * "method": "thing.event.property.post" }
 * 
 * @author zhensheng.cai
 * @date 2018/7/10.
 */
public class MeasurepointPostRequest extends BaseMqttRequest<MeasurepointPostResponse>
{
	private static final long serialVersionUID = 4018722889739885894L;

	public static  MeasurepointPostRequest.Builder builder(){
		return new Builder();
	}

	public static class Builder extends BaseMqttRequest.Builder<Builder,MeasurepointPostRequest>{
		private Map<String, Map<String, Object>> params = new HashMap<>();

		public Builder addMeasurePoint(String key, Object value){
			return this.addMeasurePoint(key, value, System.currentTimeMillis());
		}

		public Builder addMeasurePoint(String key, Object value, long timestamp)
		{
			Map<String, Object> pointValue = new HashMap<>();
			pointValue.put("value", value);
			pointValue.put("time", timestamp);
			params.put(key, pointValue);
			return this;
		}

		@Override protected String createMethod()
		{
			return MethodConstants.MEASUREPOINT_POST;
		}

		@Override protected Object createParams()
		{
			return params;
		}

		@Override protected MeasurepointPostRequest createRequestInstance()
		{
			return new MeasurepointPostRequest();
		}
	}

	private MeasurepointPostRequest()
	{
	}


	@Override
	public Class<MeasurepointPostResponse> getAnswerType()
	{
		return MeasurepointPostResponse.class;
	}

    @Override
    protected String _getPK_DK_FormatTopic()
    {
        return DeliveryTopicFormat.MEASUREPOINT_POST;
    }

}
