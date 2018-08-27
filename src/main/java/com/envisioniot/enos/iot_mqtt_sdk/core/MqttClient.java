package com.envisioniot.enos.iot_mqtt_sdk.core;

import com.envisioniot.enos.iot_mqtt_sdk.core.exception.EnvisionException;
import com.envisioniot.enos.iot_mqtt_sdk.core.internals.MqttConnection;
import com.envisioniot.enos.iot_mqtt_sdk.core.msg.IMessageHandler;
import com.envisioniot.enos.iot_mqtt_sdk.core.msg.IMqttArrivedMessage;
import com.envisioniot.enos.iot_mqtt_sdk.core.msg.IMqttRequest;
import com.envisioniot.enos.iot_mqtt_sdk.core.msg.IMqttResponse;
import com.envisioniot.enos.iot_mqtt_sdk.core.profile.Profile;
import com.envisioniot.enos.iot_mqtt_sdk.util.StringUtil;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Envision Mqtt Sdk
 * @author zhensheng.cai
 * @date 2018/7/3.
 */
public class MqttClient
{
	private AtomicLong requestId = new AtomicLong(0);
	private final MqttConnection connection;
	private Profile profile;

	/**
	 *
	 * @param uri mqtt broker server uri
	 * @param productKey productKey of login device credential
	 * @param deviceKey deviceKey
	 * @param deviceSecret deviceSecret
	 */
	public MqttClient(String uri, String productKey, String deviceKey, String deviceSecret)
	{
		profile = new Profile(uri, productKey, deviceKey, deviceSecret);
		connection = new MqttConnection(profile);
	}

	/**
	 * init with client profile
	 * @param profile client config profile
	 */
	public MqttClient(Profile profile){
		connection = new MqttConnection(profile);
	}

	public Profile getProfile(){
		return this.profile;
	}

	/**
	 * publish the request and NOT care the respsonse
	 * @throws Exception
	 */
	public <T extends IMqttResponse> void fastPublish(IMqttRequest<T> request) throws Exception
	{
		fillRequest(request);
		request.check();
		this.connection.fastPublish(request);
	}

	/**
	 * publish the sync request and wait for the response
	 * @throws Exception
	 */
	public <T extends IMqttResponse> T publish(IMqttRequest<T> request) throws Exception
	{
		// 1. do register IMessageCallback
		fillRequest(request);
		request.check();
		// 2. do post
		return connection.publish(request);
	}

	/**
	 * publish the request and register the callback , the callback will be called when rcv the response
	 * @throws Exception
	 */
	public <T extends IMqttResponse> void publish(IMqttRequest<T> request, IResponseCallback<T> callback)
			throws Exception
	{
		this.fillRequest(request);
		request.check();

		connection.publish(request, callback);
	}

	/**
	 * set the msg handler for specific arrived msg
	 */
	public void setArrivedMsgHandler(Class<? extends IMqttArrivedMessage> arrivedMsgCls, IMessageHandler<?, ?> handler)
	{
		connection.getProcessor().setArrivedMsgHandler(arrivedMsgCls, handler);
	}


	/**
	 * connect with the callback
	 * @param callback callback called when connect success, failed or connection lost
	 * @throws EnvisionException
	 */
	public void connect(IConnectCallback callback) throws EnvisionException
	{
		this.connection.connect(callback);
	}

	public void disconnect() throws EnvisionException
	{
		this.connection.disconnect();
	}

	public void close() throws EnvisionException
	{
		this.connection.close();
	}

	public boolean isConnected()
	{
		return this.connection.isConnected();
	}

	private void fillRequest(IMqttRequest<?> request)
	{
		if (StringUtil.isEmpty(request.getMessageId()))
		{
			request.setMessageId(String.valueOf(requestId.incrementAndGet()));
		}

		if (StringUtil.isEmpty(request.getVersion()))
		{
			request.setVersion(Profile.VERSION);
		}

		if (StringUtil.isEmpty(request.getProductKey()) && StringUtil.isEmpty(request.getDeviceKey()))
		{
			request.setProductKey(profile.getProductKey());
			request.setDeviceKey(profile.getDeviceKey());
		}
	}
}
