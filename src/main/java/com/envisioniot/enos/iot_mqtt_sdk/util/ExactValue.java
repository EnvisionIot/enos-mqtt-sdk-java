package com.envisioniot.enos.iot_mqtt_sdk.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhensheng.cai
 * @date 2018/11/13.
 */
public class ExactValue {
    public Object value;

    public ExactValue() {
    }

    public ExactValue(Object value) {
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    public Object get(){
        Object ret;
        if(value instanceof ExactValue){
            ret = value;
        }
        else if(value instanceof List){
            List<Object> listRet = new ArrayList<>();
            ((List) value).forEach(obj->{
                if(obj instanceof ExactValue){
                    listRet.add(((ExactValue) obj).get());
                }
                else {
                    listRet.add(obj);
                }
            });
            ret = listRet;
        }
        else if(value instanceof Map){
            Map<String, Object> mapRet = new HashMap<>();
            ((Map) value).forEach((key,obj)->{
                if(obj instanceof ExactValue){
                    mapRet.put(key.toString(), ((ExactValue) obj).get());
                }
                else {
                    mapRet.put(key.toString(), obj);
                }

            });
            ret = mapRet;
        }
        else {
            ret = value;
        }
        return ret;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
