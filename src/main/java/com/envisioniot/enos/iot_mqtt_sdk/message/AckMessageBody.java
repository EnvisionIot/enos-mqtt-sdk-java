package com.envisioniot.enos.iot_mqtt_sdk.message;

import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.ResponseCode;
import com.envisioniot.enos.iot_mqtt_sdk.util.GsonUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhensheng.cai
 * @date 2018/8/13.
 */
public class AckMessageBody implements Serializable {
    private static final long serialVersionUID = -2367357179961511079L;
    private String id;
    private int code;
    private Object data;
    private String message;

    public byte[] encode() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("code", getCode() == 0 ? ResponseCode.SUCCESS : getCode());
        if (getId() != null) {
            payload.put("id", getId());
        }
        if (getData() != null) {
            payload.put("data", getData());
        }
        if (getMessage() != null) {
            payload.put("message", getMessage());
        }
        return GsonUtil.toJson(payload).getBytes();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @SuppressWarnings("unchecked")
    public <T> T getData() {
        return (T) data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "AckMessageBody{" + "id='" + id + '\'' + ", code=" + code + ", data=" + data + ", message='" + message
                + '\'' + '}';
    }
}
