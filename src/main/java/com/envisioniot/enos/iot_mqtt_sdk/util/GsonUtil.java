package com.envisioniot.enos.iot_mqtt_sdk.util;

import com.envisioniot.enos.iot_mqtt_sdk.message.AnswerableMessageBody;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;

/**
 * @author zhensheng.cai
 * @date 2018/6/19.
 */
public class GsonUtil {
    private static Gson gson = new Gson();

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        return gson.fromJson(json, typeOfT);
    }

    public static JsonElement toJsonTree(Object src) {
        return gson.toJsonTree(src);
    }

    public static void main(String[] args) {
        AnswerableMessageBody msg = new AnswerableMessageBody();
        msg.setId("123");
        AnswerableMessageBody nMsg = GsonUtil.fromJson(new String(msg.encode()), AnswerableMessageBody.class);
        System.out.println(nMsg);
    }
}
