package com.envisioniot.enos.iot_mqtt_sdk.core;

import com.envisioniot.enos.iot_mqtt_sdk.core.msg.IMqttResponse;


/**
 * Called when received the response message
 *
 * @author zhensheng.cai
 * @date 2018/7/19.
 */
public interface IResponseCallback<T extends IMqttResponse> {
    /**
     * handle the async response of the mqtt request
     *
     * @param response
     */
    void onResponse(T response);
}
