package com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants;

import java.util.regex.Pattern;

/**
 *
 * Arrived msg topic pattern to match the decoder
 * 
 * @author zhensheng.cai
 * @date 2018/7/10.
 */
public class ArrivedTopicPattern
{

	/**
	 * 上行请求的返回
	 */
	// public static final String MEASUREPOINT_POST_REPLY =
	// "/sys/(.*)/(.*)/thing/event/measurepoint/post_reply";
	public static final String MEASUREPOINT_POST_REPLY = "/sys/(.*)/(.*)/thing/event/measurepoint/post_reply";
	public static final String EVENT_POST_REPLY = "/sys/(.*)/(.*)/thing/event/(.*)/post_reply";
	// public static final String TSL_TEMPLATE_GET_REPLY =
	// "/sys/(.*)/(.*)/thing/tsltemplate/get_reply";
	public static final String TSL_TEMPLATE_GET_REPLY = "/sys/(.*)/(.*)/thing/tsltemplate/get_reply";
	public static final String MODEL_UP_RAW_REPLY = "/sys/(.*)/(.*)/thing/model/up_raw_reply";

	public static final String SUB_DEVICE_REGISTER_REPLY = "/sys/(.*)/(.*)/thing/sub/register_reply";

	public static final String TOPO_ADD_REPLY = "/sys/(.*)/(.*)/thing/topo/add_reply";
	public static final String TOPO_DELETE_REPLY = "/sys/(.*)/(.*)/thing/topo/delete_reply";
	public static final String TOPO_GET_REPLY = "/sys/(.*)/(.*)/thing/topo/get_reply";

	public static final String SUB_DEVICE_LOGIN_REPLY = "/ext/session/(.*)/(.*)/combine/login_reply";
	public static final String SUB_DEVICE_LOGOUT_REPLY = "/ext/session/(.*)/(.*)/combine/logout_reply";

	public static final String TAG_UPDATE_REPLY = "/sys/(.*)/(.*)/thing/deviceinfo/update_reply";
	public static final String TAG_DELETE_REPLY = "/sys/(.*)/(.*)/thing/deviceinfo/delete_reply";

	/**
	 * 下行控制指令
	 */
	public static final String MEASUREPOINT_SET_COMMAND = "/sys/(.*)/(.*)/thing/service/measurepoint/set";
	public static final String SERVICE_INVOKE_COMMAND = "/sys/(.*)/(.*)/thing/service/(.*)";
	public static final String MODEL_DOWN_RAW_COMMAND = "/sys/(.*)/(.*)/thing/model/down_raw";
	public static final String MEASUREPOINT_GET_COMMAND = "/sys/(.*)/(.*)/thing/service/measurepoint/get";
	public static final String RRPC_COMMAND = "/sys/(.*)/(.*)/rrpc/request/(.*)";

	public static final String DELETE_DEVICE_COMMAND = "/sys/(.*)/(.*)/thing/delete";
	public static final String ENABLE_DEVICE_COMMAND = "/sys/(.*)/(.*)/thing/enable";
	public static final String DISABLE_DEVICE_COMMAND = "/sys/(.*)/(.*)/thing/disable";


}
