package com.envisioniot.enos.iot_mqtt_sdk.core.profile;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.SignUtil;
import com.envisioniot.enos.iot_mqtt_sdk.util.SecureModeUtil;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class BaseProfile {

    public static final String VERSION = "1.1";


    protected Config config = new Config();

    private ExecutorService executorService;

    private long timestamp = System.currentTimeMillis();
    private SSLContext sslContext = null;

    /**
     * reload the profile, when properties of the profiles is changed
     */
    public abstract void reload();


    public ExecutorService getExecutorService() {
        return executorService;
    }

    public BaseProfile setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
        return this;
    }

    public String getServerUrl() {
        return config.getServerUrl();
    }

    public String getProductKey() {
        return config.getProductKey();
    }

    public String getDeviceKey() {
        return config.getDeviceKey();
    }

    public String getDeviceSecret() {
        return config.getDeviceSecret();
    }

    public BaseProfile setProductKey(String productKey) {
        this.config.setProductKey(productKey);
        return this;
    }

    public BaseProfile setProductSecret(String productSecret) {
        this.config.setProductSecret(productSecret);
        return this;
    }

    public BaseProfile setDeviceKey(String deviceKey) {
        this.config.setDeviceKey(deviceKey);
        return this;
    }

    public BaseProfile setDeviceSecret(String deviceSecret) {
        this.config.setDeviceSecret(deviceSecret);
        return this;
    }

    public List<DeviceCredential> getSubDevices() {
        return this.config.getSubDevices();
    }

    public BaseProfile setSubDevices(List<DeviceCredential> subDevices) {
        this.config.setSubDevices(subDevices);
        return this;
    }

    /**
     * 不检查是否重复
     *
     * @param subDevice
     * @return
     */
    public BaseProfile addSubDevice(DeviceCredential subDevice) {
        this.config.getSubDevices().add(subDevice);
        return this;
    }

    /**
     * 检查是否重复
     *
     * @param subDevice
     * @return
     */
    public BaseProfile updateOrAddSubDevice(DeviceCredential subDevice) {
        for (DeviceCredential device : this.config.getSubDevices()) {
            if (device.productKey.equals(subDevice.productKey) && device.deviceKey.equals(subDevice.deviceKey)) {
                device.copyIfAbsent(subDevice);
                return this;
            }
        }
        this.config.getSubDevices().add(subDevice);
        return this;
    }

    public static ExecutorService createDefaultExecutorService() {
        return new ThreadPoolExecutor(10, 20, 0, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(10240),
                new ThreadFactoryBuilder().setNameFormat("command-pool-%d").build());
    }

    public MqttConnectOptions createConnectOptions() {
        String mqttUsername = config.getDeviceKey() + '&' + config.getProductKey();

        Map<String, String> params = new HashMap<String, String>();
        params.put("productKey", getProductKey());
        params.put("deviceKey", getDeviceKey());
        params.put("clientId", config.getDeviceKey());
        params.put("timestamp", timestamp + "");
        int securemode = getSecureMode();
        String mqttPassword = "";
        if (securemode == SecureModeUtil.VIA_DEVICE_SECRET) {
            mqttPassword = SignUtil.sign(config.getDeviceSecret(), params);
        }
        else if (securemode == SecureModeUtil.VIA_PRODUCT_SECRET) {
            mqttPassword = SignUtil.sign(config.getProductSecret(), params);
        }
        MqttConnectOptions connectOptions = new MqttConnectOptions();
        connectOptions.setUserName(mqttUsername);
        connectOptions.setPassword(mqttPassword.toCharArray());
        connectOptions.setKeepAliveInterval(this.getKeepAlive());
        connectOptions.setAutomaticReconnect(this.config.getAutoReconnect());
        connectOptions.setConnectionTimeout(this.getConnectionTimeout());
        connectOptions.setMaxInflight(this.config.getMaxInFlight());

        if (config.getSslSecured()) {
            if (this.sslContext == null) {
                try {
                    this.sslContext = createContext(this.config.getSslJksPath(), this.config.getSslPassword().toCharArray(), this.config.getSslAlgorithm());

                } catch (Exception e) {
                    throw new RuntimeException("create SSL context failed", e.fillInStackTrace());
                }
            }
            connectOptions.setSocketFactory(this.sslContext.getSocketFactory());
        }

        return connectOptions;
    }


    public int getKeepAlive() {
        return config.getKeepAlive();
    }

    /**
     * set connection keepAlive SECONDS
     *
     * @param keepAlive seconds
     * @return
     */
    public BaseProfile setKeepAlive(int keepAlive) {
        this.config.setKeepAlive(keepAlive);
        return this;
    }

    public int getConnectionTimeout() {
        return this.config.getConnectionTimeout();
    }

    /**
     * set connection timeout SECONDS
     *
     * @param connectionTimeout seconds
     * @return
     */
    public BaseProfile setConnectionTimeout(int connectionTimeout) {
        this.setConnectionTimeout(connectionTimeout);
        return this;
    }

    public int getTimeToWait() {
        return this.config.getOperationTimeout();
    }

    /**
     * @param timeToWait SECONDS to wait for the response message
     */
    public BaseProfile setTimeToWait(int timeToWait) {
        this.config.setOperationTimeout(timeToWait);
        return this;
    }

    public String getClientId() {
        return this.config.getDeviceKey() + "|securemode=" + getSecureMode() + ",signmethod=" + SignUtil.hmacsha1 + ",timestamp=" + timestamp + "|";
    }


    public BaseProfile setMaxInFlight(int maxInFlight) {
        this.config.setMaxInFlight(maxInFlight);
        return this;
    }

    public BaseProfile setAutoReconnect(boolean autoReconnect) {
        this.config.setAutoReconnect(autoReconnect);
        return this;
    }

    public BaseProfile setSSLSecured(boolean sslSecured) {
        this.config.setSslSecured(sslSecured);
        return this;
    }


    public BaseProfile setSSLContext(SSLContext sslContext) {
        this.sslContext = sslContext;
        return this;
    }

    public int getSecureMode() {
        return SecureModeUtil.getSecureMode(this.config.getDeviceSecret(), this.config.getProductSecret());
    }


    public BaseProfile setSSLJksPath(String sslJksPath, String sslPassword) {
        this.config.setSslJksPath(sslJksPath);
        this.config.setSslPassword(sslPassword);
        return this;
    }

    public BaseProfile setSSLAlgorithm(String sslAlgorithm) {
        this.config.setSslAlgorithm(sslAlgorithm);
        return this;
    }


    private static SSLContext createContext(String keyPath, char[] pwd, String algorithm) throws Exception {
        SSLContext context = SSLContext.getInstance("TLS");
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(keyPath), pwd);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(algorithm);
        tmf.init(ks);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(algorithm);
        kmf.init(ks, pwd);
        context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        return context;
    }


}
