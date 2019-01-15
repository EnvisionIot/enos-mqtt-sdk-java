package com.envisioniot.enos.iot_mqtt_sdk.core.profile;

import com.envisioniot.enos.iot_mqtt_sdk.util.StringUtil;

import java.io.Serializable;

/**
 * @author zhensheng.cai
 * @date 2019/1/14.
 */
public class DeviceCredential implements Serializable {

    private static final long serialVersionUID = -2073358271571983594L;
    public String productKey;
    public String productSecret;
    public String deviceKey;
    public String deviceSecret;

    public DeviceCredential(String productKey, String productSecret, String deviceKey, String deviceSecret) {
        this.productKey = productKey;
        this.productSecret = productSecret;
        this.deviceKey = deviceKey;
        this.deviceSecret = deviceSecret;
    }

    public void copyIfAbsent(DeviceCredential device){
        if (StringUtil.isEmpty(this.productKey)) {
            this.productKey = device.productKey;
        }
        if(StringUtil.isEmpty(this.deviceKey)){
            this.deviceKey = device.deviceKey;
        }
        if(StringUtil.isEmpty(this.deviceSecret)){
            this.deviceSecret = device.deviceSecret;
        }
        if(StringUtil.isEmpty(this.productSecret)) {
            this.productSecret = device.productSecret;
        }
    }

    public int getSecureMode(){
        if (StringUtil.isNotEmpty(deviceSecret)) {
            return 2;
        }
        if(StringUtil.isNotEmpty(productSecret)){
            return 3;
        }
        throw new IllegalArgumentException("deviceSecret or productSecret should be provided");
    }
}
