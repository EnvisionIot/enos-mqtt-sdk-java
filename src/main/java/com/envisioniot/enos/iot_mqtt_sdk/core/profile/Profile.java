package com.envisioniot.enos.iot_mqtt_sdk.core.profile;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.SignUtil;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Profile {
    public static final String VERSION = "1.0";
    private static final int DEFAULT_KEEP_ALIVE_INTERVAL = 60;
    private static final int DEFAULT_CONNECTION_TIMEOUT = 30;
    private static final int DEFAULT_OPERATION_TIMEOUT = 60;
    private String regionURL;

    private String productKey;
    private String deviceKey;
    private String deviceSecret;
    /**/
    private ExecutorService executorService;
    //    private MqttConnectOptions connectOptions;

    private int keepAlive;
    private int connectionTimeout;
    private int timeToWait;
    private long timestamp = System.currentTimeMillis();
    private boolean sslSecured = false;
    private SSLContext sslContext;

    private String sslJksPath = "";
    private String sslAlgorithm = "SunX509";
    private String sslPassword = "";

    public Profile(String regionURL, String productKey, String deviceKey, String deviceSecret) {
        this(regionURL, productKey, deviceKey, deviceSecret, createDefaultExecutorService());
    }


    public Profile(String regionURL, String productKey, String deviceKey, String deviceSecret,
                   ExecutorService executorService) {
        super();
        this.regionURL = regionURL;
        this.productKey = productKey;
        this.deviceKey = deviceKey;
        this.deviceSecret = deviceSecret;
        this.keepAlive = DEFAULT_KEEP_ALIVE_INTERVAL;
        this.connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;
        this.timeToWait = DEFAULT_OPERATION_TIMEOUT;
        this.executorService = executorService;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public Profile setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
        return this;
    }

    public String getRegionURL() {
        return regionURL;
    }

    public String getProductKey() {
        return productKey;
    }

    public String getDeviceKey() {
        return deviceKey;
    }

    public String getDeviceSecret() {
        return deviceSecret;
    }

    public static ExecutorService createDefaultExecutorService() {
        return new ThreadPoolExecutor(10, 20, 0, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(10240),
                new ThreadFactoryBuilder().setNameFormat("command-pool-%d").build());
    }

    public MqttConnectOptions createConnectOptions() {
        String mqttUsername = deviceKey + '&' + productKey;

        Map<String, String> params = new HashMap<String, String>();
        params.put("productKey", getProductKey());
        params.put("deviceKey", getDeviceKey());
        params.put("clientId", deviceKey);
        params.put("timestamp", timestamp + "");
        String mqttPassword = SignUtil.sign(deviceSecret, params);

        MqttConnectOptions connectOptions = new MqttConnectOptions();
        connectOptions.setUserName(mqttUsername);
        connectOptions.setPassword(mqttPassword.toCharArray());
        connectOptions.setKeepAliveInterval(this.getKeepAlive());
        connectOptions.setAutomaticReconnect(true);
        connectOptions.setConnectionTimeout(this.getConnectionTimeout());
        if (sslSecured) {
            if(this.sslContext == null ){
                try {
                    this.sslContext = createContext(this.sslJksPath, this.sslPassword.toCharArray(), this.sslAlgorithm);
                } catch (Exception e) {
                    throw new RuntimeException("create SSL context failed", e.fillInStackTrace());
                }
            }
            connectOptions.setSocketFactory(this.sslContext.getSocketFactory());
        }

        return connectOptions;
    }





    public int getKeepAlive() {
        return keepAlive;
    }

    /**
     * set connection keepAlive SECONDS
     *
     * @param keepAlive seconds
     * @return
     */
    public Profile setKeepAlive(int keepAlive) {
        this.keepAlive = keepAlive;
        return this;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    /**
     * set connection timeout SECONDS
     *
     * @param connectionTimeout seconds
     * @return
     */
    public Profile setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    public int getTimeToWait() {
        return timeToWait;
    }

    /**
     * @param timeToWait SECONDS to wait for the response message
     */
    public Profile setTimeToWait(int timeToWait) {
        this.timeToWait = timeToWait;
        return this;
    }

    public String getClientId() {
        return deviceKey + "|securemode=2,signmethod=" + SignUtil.hmacsha1 + ",timestamp=" + timestamp + "|";
    }

    public Profile setSSLSecured(boolean sslSecured) {
        this.sslSecured = sslSecured;
        return this;
    }

    public Profile setSSLContext(SSLContext sslContext) {
        this.sslContext = sslContext;
        return this;
    }

    public Profile setSSLJksPath(String sslJksPath , String sslPassword) {
        this.sslJksPath = sslJksPath;
        this.sslPassword = sslPassword;
        return this;
    }

    public Profile setSSLAlgorithm(String sslAlgorithm) {
        this.sslAlgorithm = sslAlgorithm;
        return this;
    }


    private static SSLContext createContext(String keyPath , char[] pwd, String algorithm) throws Exception {
        SSLContext context = SSLContext.getInstance("TLS");
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(keyPath), pwd);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(algorithm);
        tmf.init(ks);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(algorithm);
        kmf.init(ks,pwd);
        context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        return context;
    }


}
