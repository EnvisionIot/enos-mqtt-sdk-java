package com.envisioniot.enos.iot_mqtt_sdk.core.msg;

import com.envisioniot.enos.iot_mqtt_sdk.core.exception.EnvisionException;

/**
 * including upstream request {@link IMqttRequest} and the reply of downstream
 * command {@link IMqttReply}
 *
 * @author zhensheng.cai on 2018/7/3.
 */
public interface IMqttDeliveryMessage extends IMqttMessage
{

	/**
	 * implement the parameter validation by each request
	 * 
	 * @return
	 */
	void check() throws EnvisionException;

	/**
	 * get the payload of the message
	 * 
	 * @return
	 */
	byte[] encode();

	/**
	 * get the qos of publish message
	 * 
	 * @return
	 */
	int getQos();

	@Override default void setMessageTopic(String topic)
	{
		throw new UnsupportedOperationException("answer message type can't set topic");
	}
}
