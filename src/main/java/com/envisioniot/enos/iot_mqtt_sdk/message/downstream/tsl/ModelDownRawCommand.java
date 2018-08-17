package com.envisioniot.enos.iot_mqtt_sdk.message.downstream.tsl;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.ArrivedTopicPattern;
import com.envisioniot.enos.iot_mqtt_sdk.core.msg.IMqttArrivedMessage;
import com.envisioniot.enos.iot_mqtt_sdk.message.downstream.BaseMqttCommand;
import com.envisioniot.enos.iot_mqtt_sdk.util.CodingUtil;
import com.envisioniot.enos.iot_mqtt_sdk.util.MsgDecodeUtil;

/**
 * 数据流转Topic为{productKey}/{deviceName}/thing/downlink/reply/message。
 * 
 * @author zhensheng.cai
 * @date 2018/7/12.
 */
public class ModelDownRawCommand extends BaseMqttCommand<ModelDownRawReply>
{
	private static final long serialVersionUID = -2665555261792974946L;
	private static Pattern pattern = Pattern.compile(ArrivedTopicPattern.MODEL_DOWN_RAW_COMMAND);


	public byte[] getRawContent(){
		return ((byte[]) getParams());
	}

	public void setRawContent(byte[] rawContent){
		this.setParams(rawContent);
	}

    @Override
    public Class<ModelDownRawReply> getAnswerType()
    {
        return ModelDownRawReply.class;
    }


    @Override
    public Pattern getMatchTopicPattern()
    {
		return pattern;
	}
    

    @Override
	public DecodeResult decode(String topic, byte[] payload)
    {
		List<String> path = MsgDecodeUtil.match(this, topic);
		if(path == null)
		{
			return null;
		}

		ModelDownRawCommand cmd;
		try
		{
			ByteBuffer buffer = ByteBuffer.wrap(payload);
			cmd = new ModelDownRawCommand();
			cmd.setMessageId(CodingUtil.readString(buffer));
			cmd.setVersion(CodingUtil.readString(buffer));
			cmd.setMethod(CodingUtil.readString(buffer));
			cmd.setParams(CodingUtil.readBytes(buffer));
		}
		catch(Exception e)
		{
			throw new RuntimeException("decode payload err:" + Arrays.toString(payload), e);
		}

		if(cmd == null)
		{
			throw new RuntimeException("can't decode payload:" + Arrays.toString(payload));
		}

		cmd.setMessageTopic(topic);

		if(path.size() > 0)
		{
			cmd.setProductKey(path.get(0));
		}

		if(path.size() > 1)
		{
			cmd.setDeviceKey(path.get(1));
		}
		return new DecodeResult( cmd, path);
	}
}
