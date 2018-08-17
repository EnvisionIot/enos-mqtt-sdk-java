package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.status;

import java.util.Map;

import com.envisioniot.enos.iot_mqtt_sdk.core.exception.EnvisionException;
import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.DeliveryTopicFormat;
import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.MethodConstants;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.BaseMqttRequest;
import com.envisioniot.enos.iot_mqtt_sdk.util.CheckUtil;
import com.google.common.collect.Maps;

/**
 * Description: sub-device login request
 *
 * @author zhonghua.wu
 * @create 2018-07-09 14:39
 */
public class SubDeviceLogoutRequest extends BaseMqttRequest<SubDeviceLogoutResponse>
{
	// private static final String method = "combine.logout";

	private String subProductKey;
	private String subDeviceKey;

	public SubDeviceLogoutRequest() {
		this.setMethod(MethodConstants.SUB_DEVICE_LOGOUT);
	}

	public SubDeviceLogoutRequest(String subProductKey, String subDeviceKey)
	{
		this.subProductKey = subProductKey;
		this.subDeviceKey = subDeviceKey;
		this.setMethod(MethodConstants.SUB_DEVICE_LOGOUT);
	}

	@Override
	public Object getParams()
	{
		Map<String, String> params = Maps.newHashMap();
		params.put("productKey", subProductKey);
		params.put("deviceKey", subDeviceKey);
		return params;
	}

	@Override
	public void check() throws EnvisionException
	{
		CheckUtil.checkProductKey(subProductKey);
		CheckUtil.checkDeviceKey(subDeviceKey);
	}


	@Override
	public Class<SubDeviceLogoutResponse> getAnswerType()
	{
		return SubDeviceLogoutResponse.class;
	}

	public String getSubProductKey()
	{
		return subProductKey;
	}

	public void setSubProductKey(String subProductKey)
	{
		this.subProductKey = subProductKey;
	}

	public String getSubDeviceKey()
	{
		return subDeviceKey;
	}

	public void setSubDeviceKey(String subDeviceKey)
	{
		this.subDeviceKey = subDeviceKey;
	}

    @Override
    protected String _getPK_DK_FormatTopic()
    {
        return DeliveryTopicFormat.SUB_DEVICE_LOGOUT_TOPIC_FMT;
    }
}
