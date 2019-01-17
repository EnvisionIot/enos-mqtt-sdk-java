package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.ota;

import java.io.Serializable;

/**
 * Created by changyi.yuan on 2019/1/16.
 */
public class Firmware implements Serializable {
    private static final long serialVersionUID = -4861213321633614632L;

    public String version;
    public String signMethod;
    public String sign;
    public String fileUrl;
    public int fileSize;
}
