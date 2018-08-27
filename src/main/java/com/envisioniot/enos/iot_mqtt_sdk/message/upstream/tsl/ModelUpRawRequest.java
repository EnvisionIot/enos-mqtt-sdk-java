package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.tsl;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.DeliveryTopicFormat;
import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.MethodConstants;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.BaseMqttRequest;
import com.envisioniot.enos.iot_mqtt_sdk.util.CodingUtil;

import java.io.ByteArrayOutputStream;

/**
 * @author zhensheng.cai
 * @date 2018/7/5.
 */
public class ModelUpRawRequest extends BaseMqttRequest<ModelUpRawResponse>
{
	private static final long serialVersionUID = -8326661698539822393L;

	public static Builder builder(){
		return new Builder();
	}

	public static class Builder extends BaseMqttRequest.Builder<Builder,ModelUpRawRequest>
	{
		private byte[] rawContent;
		public void setRawContent(byte[] rawContent){
			this.rawContent = rawContent;
		}

		@Override protected String createMethod()
		{
			return MethodConstants.THING_MODEL_UP_RAW;
		}

		@Override protected Object createParams()
		{
			return rawContent;
		}

		@Override protected ModelUpRawRequest createRequestInstance()
		{
			return new ModelUpRawRequest();
		}
	}

	private ModelUpRawRequest()
	{
	}

	@Override
	public Class<ModelUpRawResponse> getAnswerType()
	{
		return ModelUpRawResponse.class;
	}


	/**
	 * id : String -> encode int (length) :encode String
	 * version : String -> encode int(length) : encode string
	 * method : same as version
	 * params/rawContent  : byte[] -> encode int(length) : encode byte[]
	 * @return
	 */
	@Override
	public byte[] encode(){
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CodingUtil.paddingString(bos, getMessageId());
		CodingUtil.paddingString(bos, getVersion());
		CodingUtil.paddingString(bos, getMethod());
		CodingUtil.paddingBytes(bos, getParams());
		return bos.toByteArray();
	}

	
    @Override
    protected String _getPK_DK_FormatTopic()
    {
        return DeliveryTopicFormat.MODEL_UP_RAW;
    }
}
