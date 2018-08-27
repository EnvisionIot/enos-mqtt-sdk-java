package com.envisioniot.enos.iot_mqtt_sdk.message.upstream;

import com.envisioniot.enos.iot_mqtt_sdk.core.exception.EnvisionException;
import com.envisioniot.enos.iot_mqtt_sdk.core.msg.IMqttRequest;
import com.envisioniot.enos.iot_mqtt_sdk.message.BaseAnswerableMessage;
import com.envisioniot.enos.iot_mqtt_sdk.util.CheckUtil;
import com.envisioniot.enos.iot_mqtt_sdk.util.StringUtil;

/**
 * @author zhensheng.cai
 * @date 2018/7/4.
 */
public abstract class BaseMqttRequest<T extends BaseMqttResponse> extends BaseAnswerableMessage<T>
		implements IMqttRequest<T>
{
	private static final long serialVersionUID = -1782194368038165072L;
	private String productKey;
	private String deviceKey;

	protected abstract static class Builder<B extends Builder , R extends BaseMqttRequest>
	{
		private String productKey;
		private String deviceKey;

		@SuppressWarnings("unchecked")
		public  B setProductKey(String productKey)
		{
			this.productKey = productKey;
			return (B) this;
		}

		@SuppressWarnings("unchecked")
		public  B setDeviceKey(String deviceKey)
		{
			this.deviceKey = deviceKey;
			return (B) this;
		}

		protected abstract String createMethod();

		protected abstract Object createParams();

		protected abstract R createRequestInstance();

		public R build()
		{
			R request = createRequestInstance();
			if(StringUtil.isNotEmpty(productKey)){
				request.setProductKey(productKey);
			}
			if(StringUtil.isNotEmpty(deviceKey)){
				request.setDeviceKey(deviceKey);
			}
			request.setMethod(createMethod());
			request.setParams(createParams());
			return request;
		}
	}


	@Override public String getProductKey()
	{
		return productKey;
	}

	@Override public String getDeviceKey()
	{
		return deviceKey;
	}

	@Override public void setProductKey(String productKey)
	{
		this.productKey = productKey;
	}

	@Override public void setDeviceKey(String deviceKey)
	{
		this.deviceKey = deviceKey;
	}

	@Override public String getMessageId()
	{
		return getId();
	}

	@Override public void setMessageId(String msgId)
	{
		setId(msgId);
	}

	@Override public void check() throws EnvisionException
	{
		//do basic check
		CheckUtil.checkProductKey(this.getProductKey());
		CheckUtil.checkDeviceKey(this.getDeviceKey());
	}

	@Override public int getQos()
	{
		return 0;
	}

	@Override public String getMessageTopic()
	{
		return String.format(_getPK_DK_FormatTopic(), getProductKey(), getDeviceKey());
	}


	@Override public String getAnswerTopic()
	{
		return getMessageTopic() + "_reply";
	}

	protected abstract String _getPK_DK_FormatTopic();

}
