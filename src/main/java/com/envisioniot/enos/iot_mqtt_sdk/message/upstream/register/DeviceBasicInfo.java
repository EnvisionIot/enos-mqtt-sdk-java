package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.register;

import java.io.Serializable;

/**
 * Description: device basic info (iotId) (productKey) (deviceKey)
 * (deviceSecret)
 *
 * @author zhonghua.wu
 * @create 2018-07-17 10:42
 */
public class DeviceBasicInfo implements Serializable {
    private static final long serialVersionUID = -3092183058672019646L;

    public String assetId;
    public String productKey;
    public String deviceKey;
    public String deviceSecret;

}
