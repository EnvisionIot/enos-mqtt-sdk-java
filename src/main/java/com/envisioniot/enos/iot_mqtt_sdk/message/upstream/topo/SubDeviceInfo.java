package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.topo;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.SignUtil;
import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.Map;

/**
 * Description: sub-device information
 *
 * @author zhonghua.wu
 * @create 2018-07-12 15:45
 */
public class SubDeviceInfo implements Serializable
{
	private static final long serialVersionUID = -2144256254717113693L;

	private static final String DEFAULT_SIGN_METHOD = SignUtil.hmacsha1;

	private String productKey;
	private String deviceKey;
	private String clientId;
	private long timestamp;
	private String sign;
	private String signMethod;

	public SubDeviceInfo()
	{
	}

	public SubDeviceInfo(String productKey, String deviceKey, String deviceSecret)
	{
		this.productKey = productKey;
		this.deviceKey = deviceKey;
		
		this.clientId = getDefaultClientId(productKey, deviceKey);
		this.timestamp = System.currentTimeMillis();
		this.signMethod = DEFAULT_SIGN_METHOD;
		
		Map<String, String> params = Maps.newHashMap();
		params.put("productKey", productKey);
		params.put("deviceKey", deviceKey);
		params.put("clientId", clientId);
		params.put("timestamp", String.valueOf(timestamp));
		this.sign = SignUtil.sign(deviceSecret, params);
	}

	public String getClientId()
	{
		return clientId;
	}

	public void setClientId(String clientId)
	{
		this.clientId = clientId;
	}

	public long getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(long timestamp)
	{
		this.timestamp = timestamp;
	}

	public String getSignMethod()
	{
		return signMethod;
	}

	public void setSignMethod(String signMethod)
	{
		this.signMethod = signMethod;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public Map<String, String> createSignMap()
	{
		Map<String, String> params = Maps.newHashMap();
		params.put("productKey", productKey);
		params.put("deviceKey", deviceKey);
		params.put("clientId", clientId);
		params.put("timestamp", String.valueOf(timestamp));
		params.put("signMethod", this.signMethod);
		params.put("sign", sign);
		return params;
	}

	private String getDefaultClientId(String productKey, String deviceKey)
	{
		return String.format("%s.%s.%s", productKey, deviceKey, String.valueOf(System.currentTimeMillis()));
	}
	
	public String getProductKey()
	{
	    return productKey;
	}
	
	public String getDeviceKey()
	{
	    return deviceKey;
	}

	public SubDeviceInfo setProductKey(String productKey)
	{
		this.productKey = productKey;
		return this;
	}

	public SubDeviceInfo setDeviceKey(String deviceKey)
	{
		this.deviceKey = deviceKey;
		return this;
	}
}
