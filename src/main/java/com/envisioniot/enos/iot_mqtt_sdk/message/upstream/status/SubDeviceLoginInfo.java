package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.status;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.SignUtil;
import com.envisioniot.enos.iot_mqtt_sdk.util.SecureModeUtil;
import com.envisioniot.enos.iot_mqtt_sdk.util.StringUtil;
import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.Map;

/**
 * Description: sub-device information
 *
 * @author zhonghua.wu
 * @create 2018-07-12 15:45
 */
public class SubDeviceLoginInfo implements Serializable {
    private static final long serialVersionUID = -2144256254717113693L;

    private static final String DEFAULT_SIGN_METHOD = SignUtil.hmacsha1;

    private String productKey;
    private String deviceKey;

    private String clientId;
    private long timestamp;
    private String sign;
    private String signMethod = DEFAULT_SIGN_METHOD;
    private boolean cleanSession = false;
    private Map<String, String> signParams;
    private int secureMode = 2;

    public SubDeviceLoginInfo() {
    }

    public SubDeviceLoginInfo(String productKey, String deviceKey, String deviceSecret) {
        this.productKey = productKey;
        this.deviceKey = deviceKey;
        this.clientId = getDefaultClientId(productKey, deviceKey);
        this.timestamp = System.currentTimeMillis();
        signParams = Maps.newHashMap();
        signParams.put("productKey", productKey);
        signParams.put("deviceKey", deviceKey);
        signParams.put("clientId", clientId);
        signParams.put("timestamp", String.valueOf(timestamp));
        this.sign = SignUtil.sign(deviceSecret, signParams);
        this.signMethod = DEFAULT_SIGN_METHOD;
        signParams.put("signMethod", this.signMethod);
        signParams.put("sign", sign);
        signParams.put("cleanSession", String.valueOf(cleanSession));
    }

    public SubDeviceLoginInfo(String productKey, String productSecret, String deviceKey, String deviceSecret) {
        this.productKey = productKey;
        this.deviceKey = deviceKey;
        this.clientId = getDefaultClientId(productKey, deviceKey);
        this.timestamp = System.currentTimeMillis();
        signParams = Maps.newHashMap();
        signParams.put("productKey", productKey);
        signParams.put("deviceKey", deviceKey);
        signParams.put("clientId", clientId);
        signParams.put("timestamp", String.valueOf(timestamp));
        secureMode = SecureModeUtil.getSecureMode(deviceSecret, productSecret);
        if (secureMode == SecureModeUtil.VIA_DEVICE_SECRET) {
            this.sign = SignUtil.sign(deviceSecret, signParams);
        } else if (secureMode == SecureModeUtil.VIA_PRODUCT_SECRET) {
            this.sign = SignUtil.sign(productSecret, signParams);
        }
        this.signMethod = DEFAULT_SIGN_METHOD;
        signParams.put("secureMode", String.valueOf(secureMode));
        signParams.put("signMethod", this.signMethod);
        signParams.put("sign", sign);
        signParams.put("cleanSession", String.valueOf(cleanSession));
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSignMethod() {
        return signMethod;
    }

    public void setSignMethod(String signMethod) {
        this.signMethod = signMethod;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public boolean isCleanSession() {
        return cleanSession;
    }

    public void setCleanSession(boolean cleanSession) {
        this.cleanSession = cleanSession;
    }


    public Map<String, String> getSignParams() {
        return signParams;
    }

    private String getDefaultClientId(String productKey, String deviceKey) {
        return String.format("%s.%s.%s", productKey, deviceKey, String.valueOf(System.currentTimeMillis()));
    }

    public String getProductKey() {
        return productKey;
    }

    public String getDeviceKey() {
        return deviceKey;
    }

    public int getSecureMode(){
        return secureMode;
    }

}
