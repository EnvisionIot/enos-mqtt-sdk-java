package com.envisioniot.enos.iot_mqtt_sdk.core.profile;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.SignUtil;
import com.envisioniot.enos.iot_mqtt_sdk.util.StringUtil;
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

public abstract class AbstractProfile {

    public static final String VERSION = "1.1";

    private static final int DEFAULT_KEEP_ALIVE_INTERVAL = 60;
    private static final int DEFAULT_CONNECTION_TIMEOUT = 30;
    private static final int DEFAULT_OPERATION_TIMEOUT = 60;
    private static final int DEFAULT_MAX_INFLIGHT = 10000;
    protected String serverUrl;
    protected String productKey;
    protected String productSecret;
    protected String deviceKey;
    protected String deviceSecret;
    /**/
    private ExecutorService executorService;



    private int keepAlive = DEFAULT_KEEP_ALIVE_INTERVAL;
    private int connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;
    private int timeToWait = DEFAULT_OPERATION_TIMEOUT;
    private int maxInFlight = DEFAULT_MAX_INFLIGHT;
    private boolean autoReconnect = true;
    private long timestamp = System.currentTimeMillis();
    private boolean sslSecured = false;
    private SSLContext sslContext = null;

    private String sslJksPath = "";
    private String sslAlgorithm = "SunX509";
    private String sslPassword = "";

    /**
     * reload the profile, when properties of the profiles is changed
     */
    public abstract void reload();


    public ExecutorService getExecutorService() {
        return executorService;
    }

    public AbstractProfile setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
        return this;
    }

    public String getServerUrl() {
        return serverUrl;
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

    public AbstractProfile setProductKey(String productKey) {
        this.productKey = productKey;
        return this;
    }

    public AbstractProfile setProductSecret(String productSecret) {
        this.productSecret = productSecret;
        return this;
    }

    public AbstractProfile setDeviceKey(String deviceKey) {
        this.deviceKey = deviceKey;
        return this;
    }

    public AbstractProfile setDeviceSecret(String deviceSecret) {
        this.deviceSecret = deviceSecret;
        return this;
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
        int securemode = getSecureMode();
        String mqttPassword = "";
        if(securemode == 2 ){
            mqttPassword = SignUtil.sign(deviceSecret, params);
        }
        else if(securemode ==3 ) {
            mqttPassword = SignUtil.sign(productSecret, params);
        }
        MqttConnectOptions connectOptions = new MqttConnectOptions();
        connectOptions.setUserName(mqttUsername);
        connectOptions.setPassword(mqttPassword.toCharArray());
        connectOptions.setKeepAliveInterval(this.getKeepAlive());
        connectOptions.setAutomaticReconnect(autoReconnect);
        connectOptions.setConnectionTimeout(this.getConnectionTimeout());
        connectOptions.setMaxInflight(maxInFlight);

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
    public AbstractProfile setKeepAlive(int keepAlive) {
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
    public AbstractProfile setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    public int getTimeToWait() {
        return timeToWait;
    }

    /**
     * @param timeToWait SECONDS to wait for the response message
     */
    public AbstractProfile setTimeToWait(int timeToWait) {
        this.timeToWait = timeToWait;
        return this;
    }

    public String getClientId() {
        return deviceKey + "|securemode="+ getSecureMode()+",signmethod=" + SignUtil.hmacsha1 + ",timestamp=" + timestamp + "|";
    }


    public AbstractProfile setMaxInFlight(int maxInFlight){
        this.maxInFlight = maxInFlight;
        return this;
    }

    public AbstractProfile setAutoReconnect(boolean autoReconnect) {
        this.autoReconnect = autoReconnect;
        return this;
    }

    public AbstractProfile setSSLSecured(boolean sslSecured) {
        this.sslSecured = sslSecured;
        return this;
    }


    public AbstractProfile setSSLContext(SSLContext sslContext) {
        this.sslContext = sslContext;
        return this;
    }

    public int getSecureMode(){
        if (StringUtil.isNotEmpty(this.deviceSecret)) {
            return 2;
        }
        if(StringUtil.isNotEmpty(this.productSecret)){
            return 3;
        }
        throw new IllegalArgumentException("deviceSecret or productSecret should be provided");
    }


    public AbstractProfile setSSLJksPath(String sslJksPath , String sslPassword) {
        this.sslJksPath = sslJksPath;
        this.sslPassword = sslPassword;
        return this;
    }

    public AbstractProfile setSSLAlgorithm(String sslAlgorithm) {
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
