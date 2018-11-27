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

    public static final int COMMON_PRODUCT_ERR = 600;

    public static final int COMMON_DEVICE_ERR = 700;

    public static final int COMMON_DATA_ERR = 800;

    public static final int COMMON_MODEL_ERR = 900;

    public static final int COMMON_TRANSPORT_LAYER_ERR = 1000;

    public static final int COMMON_DEVICE_CLIENT_ERR = 1100;

    public static final int COMMAND_HANDLER_NOT_REGISTERED = 1101;

    public static final int COMMAND_HANDLER_EXECUTION_FAILED = 1102;

    public static final int USER_DEFINED_ERR_CODE = 2000;
}
