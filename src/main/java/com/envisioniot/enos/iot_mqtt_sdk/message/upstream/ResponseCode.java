package com.envisioniot.enos.iot_mqtt_sdk.message.upstream;

import com.envisioniot.enos.iot_mqtt_sdk.core.msg.IMqttResponse;

/**
 * response code used in {@link IMqttResponse}
 *
 * @author zhensheng.cai
 * @date 2018/7/6.
 */
public class ResponseCode {
    /**
     * 成功
     */
    public static final int SUCCESS = 200;

    public static final int COMMON_DEVICE_CLIENT_ERR = 1000;

    public static final int COMMAND_HANDLER_NOT_REGISTERED = 1001;

    public static final int COMMAND_HANDLER_EXECUTION_FAILED = 1002;

    public static final int USER_DEFINED_ERR_CODE = 2000;
}
