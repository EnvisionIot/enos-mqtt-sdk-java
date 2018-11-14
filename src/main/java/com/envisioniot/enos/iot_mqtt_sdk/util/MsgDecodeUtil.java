package com.envisioniot.enos.iot_mqtt_sdk.util;

import com.envisioniot.enos.iot_mqtt_sdk.core.msg.IMqttArrivedMessage;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

/**
 * @author zhensheng.cai
 * @date 2018/8/14.
 */
public class MsgDecodeUtil {



    public static IMqttArrivedMessage decode(IMqttArrivedMessage sampleMsg, byte[] payload) throws Exception {
        String msg = new String(payload, "UTF-8");
        return GsonUtil.fromJson(msg, sampleMsg.getClass());
    }

}
