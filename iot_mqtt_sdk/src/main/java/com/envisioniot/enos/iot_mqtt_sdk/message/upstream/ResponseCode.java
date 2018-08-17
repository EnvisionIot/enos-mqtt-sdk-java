package com.envisioniot.enos.iot_mqtt_sdk.message.upstream;

import com.envisioniot.enos.iot_mqtt_sdk.core.msg.IMqttResponse;

/**
 * response code used in {@link IMqttResponse}
 * @author zhensheng.cai
 * @date 2018/7/6.
 */
public class ResponseCode
{
    /**
     * 成功
     */
    public static final int SUCCESS = 200;
    /**
     * 内部服务错误， 处理时发生内部错误
     */
    public static final int INTERNAL_ERR = 400;

    /**
     * 请求参数错误， 设备入参校验失败
     */
    public static final int PARAMETER_ERR = 460;

    public static final int DEVICE_NOT_EXISTS = 402;

    public static final int AUTH_ERR = 401;

    /**
     * 请求过于频繁，设备端处理不过来时可以使用
     */
    public static final int TOO_MANY_REQUESTS = 429;

    /**
     * 设备端注册服务执行错误
     */
    public static final int COMMAND_HANDLER_EXECUTION_FAILED = 500;

    public static final int PAYLOAD_MUST_BE_JSON_FORMAT = 6207;

    /**
     * 服务器端超时，返回的响应码
     */
    public static final int TIME_OUT = 6205;

    /**
     *
     * 设备端等待响应超时
     */
    public static final int LOCAL_TIME_OUT = 6305;

    /**

    /**
     * RRPC的响应服务没有提供
     */
    public static final int RRPC_HANDLER_NOT_FOUND = 10001;

    /**
     * 从100000到110000用于设备自定义错误信息，和云端错误信息加以区分
     */
    public static final int DEVICE_DEFINED_ERR = 100000;

}
