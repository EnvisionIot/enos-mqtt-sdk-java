package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.topo;

import java.util.HashMap;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.DeliveryTopicFormat;
import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.MethodConstants;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.BaseMqttRequest;

/**
 * Description: get topotaxy request
 *
 * @author zhonghua.wu
 * @create 2018-07-09 14:29
 */
public class GetTopoRequest extends BaseMqttRequest<GetTopoResponse>
{
	public GetTopoRequest()
	{
		this.setMethod(MethodConstants.TOPO_GET);
		this.setParams(new HashMap<>());
	}

	@Override
	public Class<GetTopoResponse> getAnswerType()
	{
		return GetTopoResponse.class;
	}

    @Override
    protected String _getPK_DK_FormatTopic()
    {
        return DeliveryTopicFormat.TOPO_GET_TOPIC_FMT;
    }

}
