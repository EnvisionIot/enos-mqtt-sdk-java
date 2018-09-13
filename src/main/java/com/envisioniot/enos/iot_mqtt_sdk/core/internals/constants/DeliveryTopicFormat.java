package com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants;

/**
 * @author zhensheng.cai
 * @date 2018/7/12.
 */
public class DeliveryTopicFormat
{
	/**
	 * 设备端上行的请求Topic格式
	 */
	public static final String MEASUREPOINT_POST = "/sys/%s/%s/thing/event/measurepoint/post";
	public static final String EVENT_POST = "/sys/%s/%s/thing/event/%s/post";
	public static final String TSL_TEMPLATE_GET = "/sys/%s/%s/thing/tsltemplate/get";
	public static final String MODEL_UP_RAW = "/sys/%s/%s/thing/model/up_raw";

	public static final String SUB_DEVICE_REGISTER_TOPIC_FMT = "/sys/%s/%s/thing/sub/register";

	public static final String TOPO_GET_TOPIC_FMT = "/sys/%s/%s/thing/topo/get";
	public static final String TOPO_DELETE_TOPIC_FMT = "/sys/%s/%s/thing/topo/delete";
	public static final String TOPO_ADD_TOPIC_FMT = "/sys/%s/%s/thing/topo/add";

	public static final String SUB_DEVICE_LOGIN_TOPIC_FMT = "/ext/session/%s/%s/combine/login";
	public static final String SUB_DEVICE_LOGOUT_TOPIC_FMT = "/ext/session/%s/%s/combine/logout";

	public static final String TAG_DELETE_TOPIC_FMT = "/sys/%s/%s/thing/deviceinfo/delete";
	public static final String TAG_UPDATE_TOPIC_FMT = "/sys/%s/%s/thing/deviceinfo/update";

	/**
	 * 云端指令的返回Topic格式
	 */
	public static final String MEASUREPOINT_SET_REPLY = "/sys/%s/%s/thing/service/measurepoint/set_reply";
	public static final String SERVICE_INVOKE_REPLY = "/sys/%s/%s/thing/service/%s_reply";
	public static final String MODEL_DOWN_RAW_REPLY = "/sys/%s/%s/thing/model/down_raw_reply";
	public static final String MEASUREPOINT_GET_REPLY = "/sys/%s/%s/thing/service/measurepoint/get_reply";
	public static final String RRPC_REPLY = "/sys/%s/%s/rrpc/response/%s";

	public static final String ENABLE_DEVICE_REPLY = "/sys/%s/%s/thing/enable_reply";
	public static final String DISABLE_DEVICE_REPLY = "/sys/%s/%s/thing/disable_reply";
	public static final String DELETE_DEVICE_REPLY = "/sys/%s/%s/thing/delete_reply";

}
