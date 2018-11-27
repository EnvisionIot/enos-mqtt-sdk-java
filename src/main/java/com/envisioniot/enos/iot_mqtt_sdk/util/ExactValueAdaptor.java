package com.envisioniot.enos.iot_mqtt_sdk.util;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhensheng.cai
 * @date 2018/11/13.
 */
public class ExactValueAdaptor extends TypeAdapter<ExactValue> {

    public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (type.getRawType() == ExactValue.class) {
                return (TypeAdapter<T>) new ExactValueAdaptor(gson);
            }
            return null;
        }
    };

    private final Gson gson;

    public ExactValueAdaptor(Gson gson) {
        this.gson = gson;
    }


    @Override
    public void write(JsonWriter out, ExactValue value) throws IOException {
        out.beginObject();
        gson.getAdapter(Object.class).write(out, value.value);
        out.endObject();

    }

    @Override
    public ExactValue read(JsonReader in) throws IOException {
        JsonToken token = in.peek();
        ExactValue number = new ExactValue();
        switch (token) {
            case BEGIN_ARRAY:
                List<Object> list = new ArrayList<Object>();
                in.beginArray();
                while (in.hasNext()) {
                    list.add(read(in));
                }
                in.endArray();
                number.value = list;
                return number;

            case BEGIN_OBJECT:
                Map<String, Object> map = new LinkedTreeMap<String, Object>();
                in.beginObject();
                while (in.hasNext()) {
                    map.put(in.nextName(), read(in));
                }
                in.endObject();
                number.value = map;
                return number;

            case STRING:
                number.value = in.nextString();
                return number;

            case NUMBER:
                String numberStr = in.nextString();
                if (numberStr.contains(".") || numberStr.contains("e")
                        || numberStr.contains("E")) {
                    number.value =  Double.parseDouble(numberStr);
                    return number;
                }
                else {
                    if((number.value = tryParseInteger(numberStr)) != null){
                        return number;
                    }
                    if((number.value = tryParseLong(numberStr))!=null ){
                        return number;
                    }
                    if((number.value = tryParseDouble(numberStr))!= null){
                        return number;
                    }
                    throw new RuntimeException("parse number for str: " + numberStr + "failed ");
                }
            case BOOLEAN:
                number.value = in.nextBoolean();
                return number;

            case NULL:
                in.nextNull();
                number.value = null;
                return number;
            default:
                throw new IllegalStateException();
        }
    }

    public Object tryParseInteger(String numberStr){
        try{
            return Integer.valueOf(numberStr);
        }
        catch (Exception e){
            return null;
        }
    }
    public Object tryParseLong(String numberStr){
        try{
            return Long.valueOf(numberStr);
        }
        catch (Exception e){
            return null;
        }
    }

    public Object tryParseDouble(String numberStr){
        try{
            return Double.valueOf(numberStr);
        }
        catch (Exception e){
            return null;
        }
    }



    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        int parsed = Integer.valueOf(time + "");
        long parsed2 = Long.valueOf(time + "");
    }
}
