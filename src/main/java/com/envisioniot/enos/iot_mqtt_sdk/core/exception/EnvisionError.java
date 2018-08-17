package com.envisioniot.enos.iot_mqtt_sdk.core.exception;

/**
 * @author zhensheng.cai
 * @date 2018/7/12.
 */
public enum EnvisionError
{
    /**
	 *
	 */
    INIT_MQTT_CLIENT_FAILED(-100, "INIT_MQTT_CLIENT_FAILED"),
    MQTT_CLIENT_CONNECT_FAILED(-101, "MQTT_CLIENT_CONNECT_FAILED"),
    MQTT_CLIENT_PUBLISH_FAILED(-102, "MQTT_CLIENT_PUBLISH_FAILED"),
    MQTT_CLIENT_DISCONNECT_FAILED(-103, "MQTT_CLIENT_DISCONNECT_FAILED"),
    MQTT_CLIENT_SUBSCRIEBE_FAILED(-104, "MQTT_CLIENT_SUBSCRIEBE_FAILED"),
    MQTT_CLIENT_CLOSE_FAILED(-105, "MQTT_CLIENT_CLOSE_FAILED"),
    INVALID_DEVICE_CREDENTIAL(-106, "INVALID_DEVICE_CREDENTIAL"),
    INVALID_REPLY_MESSAGE_FORMAT(-107, "INVALID_REPLY_MESSAGE_FORMAT"),
    INVALID_PAYLOAD(-108, "INVALID_PAYLOAD"),
    EMPTY_PAYLOAD(-109, "EMPTY_PAYLOAD"),
    GET_LOCAL_MODEL_FAILED(-110, "GET_LOCAL_MODEL_FAILED"),
    MODEL_VALIDATION_FAILED(-111, "MODEL_VALIDATION_FAILED"),
    RESPONSE_PARSE_ERR(-112, "RESPONSE_PARSE_ERR"),
    MQTT_RESPONSE_PARSED_FALED(-113, "MQTT_RESPONSE_PARSED_FALED"),
    UNSUPPPORTED_REQUEST_CALL_TYPE(-114, "UNSUPPPORTED_REQUEST_CALL_TYPE"),
    SESSION_IS_NULL(-115, "SESSION_IS_NULL"),
    STATUS_IS_UNKNOWN(-116, "STATUS_IS_UNKNOWN"),
    CODE_ERROR_MISSING_ARGS(-117, "CODE_ERROR_MISSING_ARGS"),
    CODE_ERROR_ARG_INVALID(-118, "CODE_ERROR_ARG_INVALID"),
    CANNOT_REGISTER_CALLBACK(-119, "CANNOT_REGISTER_CALLBACK"),
    DEVICE_SESSION_IS_NULL(-120,"SESSION IS NULL"),
    CALLBACK_EXECUTION_FAILED(-121, "callback execution failed"),
    STATUS_ERROR(-122, "invalid operation in current status"),
    STATUS_NOT_ALLOW_LOGIN(-123, "status not allow login"),
    STATUS_NOT_ALLOW_LOGOUT(-124, "status not allow logout"),

    SUCCESS(0, "success");
    private int errorCode;
    private String errorMessage;

    EnvisionError(int errorCode, String errorMessage)
    {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public int getErrorCode()
    {
        return errorCode;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }
}
