package com.envisioniot.enos.iot_mqtt_sdk.util;

/**
 * @author zhensheng.cai
 * @date 2019/1/15.
 */
public class SecureModeUtil {

    public static final int VIA_DEVICE_SECRET = 2;
    public static final int VIA_PRODUCT_SECRET = 3;

    public static int getSecureMode(String deviceSecret , String productSecret){
        if(StringUtil.isNotEmpty(deviceSecret) ){
            return VIA_DEVICE_SECRET;
        }
        if(StringUtil.isNotEmpty(productSecret)){
            return VIA_PRODUCT_SECRET;
        }
        throw new IllegalArgumentException("deviceSecret or productSecret should be provided");
    }
}
