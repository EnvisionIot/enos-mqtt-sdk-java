package com.envisioniot.enos.iot_mqtt_sdk.util;

import com.envisioniot.enos.iot_mqtt_sdk.message.AnswerableMessageBody;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.*;

/**
 * @author zhensheng.cai
 * @date 2018/6/19.
 */
public class GsonUtil {
    private static Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(ExactValueAdaptor.FACTORY)
            .create();

    private static Gson prettyGson;

    private static Gson getPrettyGson(){
        if(prettyGson == null ){
            synchronized (GsonUtil.class) {
                if(prettyGson == null ){
                    prettyGson = new GsonBuilder()
                            .registerTypeAdapterFactory(ExactValueAdaptor.FACTORY)
                            .setPrettyPrinting()
                            .create();
                }
            }
        }
        return prettyGson;
    }

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public static String toPrettyJson(Object object){
        return getPrettyGson().toJson(object);
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        return gson.fromJson(json, typeOfT);
    }

    public static JsonElement toJsonTree(Object src) {
        return gson.toJsonTree(src);
    }


    private static Object tryParseInteger(String numberStr){
        try{
            return Integer.valueOf(numberStr);
        }
        catch (Exception e){
            return null;
        }
    }
    private static  Object tryParseLong(String numberStr){
        try{
            return Long.valueOf(numberStr);
        }
        catch (Exception e){
            return null;
        }
    }

    private static Object tryParseDouble(String numberStr){
        try{
            return Double.valueOf(numberStr);
        }
        catch (Exception e){
            return null;
        }
    }

    public static void main(String[] args) {
        AnswerableMessageBody msg = new AnswerableMessageBody();
        msg.setId("123");
        HashMap<String, Object> params = new HashMap<>();
        params.put("a", 1);
        params.put("b", 1.0);
        params.put("c", Lists.newArrayList(1.0, 1, "sdsd"));
        params.put("d", ImmutableMap.of("a", 1, "b", 2.0));
        msg.setParams(params);
        AnswerableMessageBody nMsg = GsonUtil.fromJson(new String(msg.encode()), AnswerableMessageBody.class);
        System.out.println(nMsg);
    }
}
