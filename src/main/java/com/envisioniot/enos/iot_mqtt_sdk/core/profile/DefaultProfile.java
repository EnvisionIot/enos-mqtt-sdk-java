package com.envisioniot.enos.iot_mqtt_sdk.core.profile;

import java.util.concurrent.ExecutorService;

/**
 * @author zhensheng.cai
 * @date 2019/1/3.
 */
public class DefaultProfile extends AbstractProfile {


    public DefaultProfile(String regionURL, String productKey, String deviceKey, String deviceSecret) {
        this(regionURL, productKey, null, deviceKey, deviceSecret, createDefaultExecutorService());
    }

    public DefaultProfile(String regionURL, String productKey, String productSecret, String deviceKey, String deviceSecret){
        this(regionURL, productKey, productSecret, deviceKey, deviceSecret ,createDefaultExecutorService());
    }

    public DefaultProfile(String regionURL, String productKey, String productSecret,  String deviceKey, String deviceSecret,
                           ExecutorService executorService) {
        super();
        this.serverUrl = regionURL;
        this.productKey = productKey;
        this.productSecret = productSecret;
        this.deviceKey = deviceKey;
        this.deviceSecret = deviceSecret;
        this.setExecutorService(executorService);
    }


    @Override
    public void reload() {
        //do nothing
    }
}
