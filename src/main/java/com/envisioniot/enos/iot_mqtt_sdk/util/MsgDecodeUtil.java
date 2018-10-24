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


    public static List<String> match(IMqttArrivedMessage sampleMsg, String topic) {
        Matcher matcher = sampleMsg.getMatchTopicPattern().matcher(topic);
        if (matcher.matches()) {
            String[] groups = new String[matcher.groupCount()];
            for (int i = 0; i < matcher.groupCount(); i++) {
                groups[i] = matcher.group(i + 1);
            }

            return Arrays.asList(groups);
        }
        return null;
    }

    public static IMqttArrivedMessage decode(IMqttArrivedMessage sampleMsg, byte[] payload) throws Exception {
        String msg = new String(payload, "UTF-8");
        return GsonUtil.fromJson(msg, sampleMsg.getClass());
    }

}
