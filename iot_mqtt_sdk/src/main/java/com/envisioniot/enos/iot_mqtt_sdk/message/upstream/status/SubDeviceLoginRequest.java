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
 * @create 2018-07-09 14:38
 */
public class SubDeviceLoginRequest extends BaseMqttRequest<SubDeviceLoginResponse>
{
	// private static final String method = "combine.login";

	private SubDeviceLoginInfo subDeviceInfo;

	public SubDeviceLoginRequest()
	{
		this.setMethod(MethodConstants.SUB_DEVICE_LOGIN);
	}

	public SubDeviceLoginRequest(SubDeviceLoginInfo subDeviceInfo)
	{
		this.subDeviceInfo = subDeviceInfo;
		this.setParams(subDeviceInfo.getSignParams());
		this.setMethod(MethodConstants.SUB_DEVICE_LOGIN);
	}

	public SubDeviceLoginInfo getSubDeviceInfo()
	{
		return subDeviceInfo;
	}

	public void setSubDeviceInfo(SubDeviceLoginInfo subDeviceInfo)
	{
		this.subDeviceInfo = subDeviceInfo;
		this.setParams(subDeviceInfo.getSignParams());
	}

	@Override
	public Map<String, String> getParams()
	{
		return this.subDeviceInfo.getSignParams();
	}

	@Override
	public void check() throws EnvisionException
	{
	    super.check();
		
	    CheckUtil.checkNotEmpty(subDeviceInfo.getProductKey(), "subDeviceInfo.productKey");
        CheckUtil.checkNotEmpty(subDeviceInfo.getDeviceKey(), "subDeviceInfo.deviceKey");
        CheckUtil.checkNotEmpty(subDeviceInfo.getClientId(), "subDeviceInfo.client");
        CheckUtil.checkNotEmpty(subDeviceInfo.getSignMethod(), "subDeviceInfo.signMethod");
        CheckUtil.checkNotEmpty(subDeviceInfo.getSign(), "subDeviceInfo.sign");
	}

	@Override
	public Class<SubDeviceLoginResponse> getAnswerType()
	{
		return SubDeviceLoginResponse.class;
	}

    @Override
    protected String _getPK_DK_FormatTopic()
    {
        return DeliveryTopicFormat.SUB_DEVICE_LOGIN_TOPIC_FMT;
    }
}
