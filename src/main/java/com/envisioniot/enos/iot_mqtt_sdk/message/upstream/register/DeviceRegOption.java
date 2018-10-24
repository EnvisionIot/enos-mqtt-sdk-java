package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.register;

import java.util.HashMap;
import java.util.Map;

/**
 * Description: sub-device register options
 *
 * @author zhonghua.wu
 * @create 2018-07-20 9:45
 */
public class DeviceRegOption {

    public String deviceKey;
    public String deviceName;
    public String deviceDesc;
    public Map<String, Object> deviceAttributes;

    public DeviceRegOption() {
    }

    public DeviceRegOption(String deviceKey, String deviceName, String deviceDesc) {
        this.deviceKey = deviceKey;
        this.deviceName = deviceName;
        this.deviceDesc = deviceDesc;
        this.deviceAttributes = new HashMap<>();
    }

    public DeviceRegOption(String deviceKey, String deviceName, String deviceDesc, Map<String, Object> deviceAttributes) {
        this.deviceKey = deviceKey;
        this.deviceName = deviceName;
        this.deviceDesc = deviceDesc;
        this.deviceAttributes = deviceAttributes;
    }
}
