package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.tsl;

import java.util.Arrays;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.DeliveryTopicFormat;
import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.MethodConstants;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.BaseMqttRequest;
import com.envisioniot.enos.iot_mqtt_sdk.util.StringUtil;

/**
 * @author zhensheng.cai
 * @date 2018/7/5.
 */
public class ModelUpRawRequest extends BaseMqttRequest<ModelUpRawResponse>
{
	private static final long serialVersionUID = -8326661698539822393L;

	private byte[] payload;

	private ModelUpRawRequest()
	{
	}

	public static Builder builder()
	{
		return new Builder();
	}

	public static class Builder extends BaseMqttRequest.Builder<Builder, ModelUpRawRequest>
	{
		public Builder()
		{
		}

		private byte[] payload;

		public Builder setPayload(byte[] payload)
		{
			this.payload = payload;
			return this;
		}

		@Override protected String createMethod()
		{
			return MethodConstants.THING_MODEL_UP_RAW;
		}

		@Override protected Object createParams()
		{
			throw new UnsupportedOperationException("unsupported operation");
		}

		@Override
		protected ModelUpRawRequest createRequestInstance(){
			return new ModelUpRawRequest();
		}

		@Override
		public ModelUpRawRequest build() {
			ModelUpRawRequest request = createRequestInstance();
			if (StringUtil.isNotEmpty(productKey)) {
				request.setProductKey(productKey);
			}
			if (StringUtil.isNotEmpty(deviceKey)) {
				request.setDeviceKey(deviceKey);
			}
			request.setMethod(createMethod());
			request.setPayload(payload);
			return request;
		}
	}

	@Override
	public String getId() {
		return "unknown";
	}

	@Override
	public void setId(String id) {
		throw new UnsupportedOperationException("cannot set raw request message id");
	}

	@Override
	public String getMessageId() {
		return "unknown";
	}

	@Override
	public void setMessageId(String msgId) {
		throw new UnsupportedOperationException("cannot set raw request message id");
	}

	@Override
	public byte[] encode()
	{
		return payload;
	}

	public ModelUpRawRequest setPayload(byte[] payload) {
		this.payload = payload;
		return this;
	}

	@Override
	public Class<ModelUpRawResponse> getAnswerType()
	{
		return ModelUpRawResponse.class;
	}

	@Override
	protected String _getPK_DK_FormatTopic()
	{
		return DeliveryTopicFormat.MODEL_UP_RAW;
	}

	@Override
	public String toString() {
		return "ModelUpRawRequest{" +
				"payload=" + Arrays.toString(payload) +
				"} ";
	}
}
