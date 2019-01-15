package com.envisioniot.enos.iot_mqtt_sdk.core.profile;

import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.register.DeviceBasicInfo;
import com.envisioniot.enos.iot_mqtt_sdk.util.GsonUtil;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 {
 //store properties by mqtt sdk
 //Thu Jan 10 14:53:58 CST 2019
 //serverUrl=tcp://alpha-k8s-cn4.eniot.io:11883
 //"assetId":"FgEWZ6Fz"
 "serverUrl":"ssl://10.27.21.6:18883",
 "productKey":"fKInRgVP",
 "productSecret":"c3PW03srd6c,
 "deviceKey":"zscai_test_activate",
 "deviceSecret":"DrihKncQARUwVXUvRH6k",
 "operationTimeout":60,
 "sslPassword":"",
 "maxInFlight":"10000",
 "sslAlgorithm":"SunX509",
 "connectionTimeout":30,
 "sslJksPath":"",
 "autoReconnect":true,
 "keepAlive":60,
 "subDevices":[
 {
 "productKey":"subProduct",
 "deviceKey":"subdevice",
 "deviceSecret":"xxx",
 "product":"secret"
 },
 {
 "productKey":"subProduct",
 "deviceKey":"subdevice",
 "deviceSecret":"xxx",
 "product":"secret"
 }
 ]
 }
 * @author zhensheng.cai
 * @date 2019/1/14.
 */
public class Config {


    private static final int DEFAULT_KEEP_ALIVE_INTERVAL = 60;
    private static final int DEFAULT_CONNECTION_TIMEOUT = 30;
    private static final int DEFAULT_OPERATION_TIMEOUT = 60;
    private static final int DEFAULT_MAX_INFLIGHT = 10000;


    private String serverUrl;
    private String productKey;
    private String deviceKey;
    private String productSecret;
    private String deviceSecret;
    private Integer operationTimeout = DEFAULT_OPERATION_TIMEOUT;
    private Integer maxInFlight;
    private Integer connectionTimeout;
    private Boolean autoReconnect = true;
    private Integer keepAlive = DEFAULT_KEEP_ALIVE_INTERVAL;

    private Boolean sslSecured = false;
    private String sslPassword="";
    private String sslAlgorithm = "SunX509";
    private String sslJksPath = "";

    private List<DeviceCredential> subDevices = new ArrayList<>();

    public Config addSubDevice(DeviceCredential subDevice){
        this.subDevices.add(subDevice);
        return this;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public Config setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
        return this;
    }

    public String getProductKey() {
        return productKey;
    }

    public Config setProductKey(String productKey) {
        this.productKey = productKey;
        return this;
    }

    public String getDeviceKey() {
        return deviceKey;
    }

    public Config setDeviceKey(String deviceKey) {
        this.deviceKey = deviceKey;
        return this;
    }

    public String getProductSecret() {
        return productSecret;
    }

    public Config setProductSecret(String productSecret) {
        this.productSecret = productSecret;
        return this;
    }

    public String getDeviceSecret() {
        return deviceSecret;
    }

    public Config setDeviceSecret(String deviceSecret) {
        this.deviceSecret = deviceSecret;
        return this;
    }

    public Integer getOperationTimeout() {
        return operationTimeout;
    }

    public Config setOperationTimeout(Integer operationTimeout) {
        this.operationTimeout = operationTimeout;
        return this;
    }

    public String getSslPassword() {
        return sslPassword;
    }

    public Config setSslPassword(String sslPassword) {
        this.sslPassword = sslPassword;
        return this;
    }

    public Integer getMaxInFlight() {
        return maxInFlight;
    }

    public Config setMaxInFlight(Integer maxInFlight) {
        this.maxInFlight = maxInFlight;
        return this;
    }

    public String getSslAlgorithm() {
        return sslAlgorithm;
    }

    public Config setSslAlgorithm(String sslAlgorithm) {
        this.sslAlgorithm = sslAlgorithm;
        return this;
    }

    public Integer getConnectionTimeout() {
        return connectionTimeout;
    }

    public Config setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    public String getSslJksPath() {
        return sslJksPath;
    }

    public Config setSslJksPath(String sslJksPath) {
        this.sslJksPath = sslJksPath;
        return this;
    }

    public Boolean getAutoReconnect() {
        return autoReconnect;
    }

    public Config setAutoReconnect(Boolean autoReconnect) {
        this.autoReconnect = autoReconnect;
        return this;
    }

    public Integer getKeepAlive() {
        return keepAlive;
    }

    public Config setKeepAlive(Integer keepAlive) {
        this.keepAlive = keepAlive;
        return this;
    }

    public List<DeviceCredential> getSubDevices() {
        return subDevices;
    }

    public Config setSubDevices(List<DeviceCredential> subDevices) {
        this.subDevices = subDevices;
        return this;
    }

    public Boolean getSslSecured() {
        return sslSecured;
    }

    public Config setSslSecured(Boolean sslSecured) {
        this.sslSecured = sslSecured;
        return this;
    }

    public void store(String path, String comments) throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append("//").append(comments).append("\n");
        builder.append("//").append(new Date().toString()).append("\n");
        builder.append(GsonUtil.toPrettyJson(this));
        Files.write(builder.toString(), new File(path), Charsets.UTF_8);
    }
}
