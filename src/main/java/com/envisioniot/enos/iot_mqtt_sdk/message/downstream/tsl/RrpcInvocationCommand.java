package com.envisioniot.enos.iot_mqtt_sdk.message.downstream.tsl;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.ArrivedTopicPattern;
import com.envisioniot.enos.iot_mqtt_sdk.message.downstream.BaseMqttCommand;
import com.envisioniot.enos.iot_mqtt_sdk.util.BASE64Coding;

import java.util.regex.Pattern;

/**
 * revert-RPC ( AliRrpc request: Action String 是 要执行的操作，取值：RRpc。 ProductKey
 * String 是 要发送消息的产品Key。 DeviceName String 是 要接收消息的设备名称。 RequestBase64Byte
 * String 是 要发送的请求消息内容经过Base64编码得到的字符串格式数据。 Timeout Integer 是
 * 等待设备回复消息的时间，单位是毫秒，取值范围是1,000 ~5,000。
 * 
 * 对于TSL服务的同步调用，在云端会转化成rrpc调用，会在云端先进行tsl的参数的检查，只有符合参数条件的请求会下发到云端，
 * 云端下发的参数，即为设备端收到的参数
 * ：{"method":"thing.service.syncservicewithparam","id":"23624456","params":{"power":100},"version":"1.0.0"},
 * topic=/sys/a1TB8qpr6bT/collect_gateway/rrpc/request/1018733446471622656
 * 
 * @author zhensheng.cai
 * @date 2018/7/13.
 */
public class RrpcInvocationCommand extends BaseMqttCommand<RrpcInvocationReply>
{
	private static final long serialVersionUID = -1268006656571832440L;
	private static Pattern pattern = Pattern.compile(ArrivedTopicPattern.RRPC_COMMAND);


	public String getContent()
	{
		return (String) this.getParams();
	}

	public byte[] getBase64DecodeContent()
	{
		return BASE64Coding.decode(getContent());
	}

    @Override
    public Class<RrpcInvocationReply> getAnswerType()
    {
        return RrpcInvocationReply.class;
    }

    @Override
    public Pattern getMatchTopicPattern()
    {
		return pattern;
	}
}
