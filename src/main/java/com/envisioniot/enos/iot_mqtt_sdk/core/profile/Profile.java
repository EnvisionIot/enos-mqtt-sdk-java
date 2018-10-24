package com.envisioniot.enos.iot_mqtt_sdk.core.profile;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.SignUtil;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
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
    private boolean useClientAuth = false;
    private String jksPath = "cChat.jks";
    private char[] jksPwd = "cNetty".toCharArray();
    private String jksAlgorithm = "SunX509";

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
        return new ThreadPoolExecutor(2, 4, 0, TimeUnit.MILLISECONDS,
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
            SSLContext ctx;
            try {
                ctx = createContext(jksPath, jksPwd, jksAlgorithm, useClientAuth);
            } catch (Exception e) {
                throw new RuntimeException("create SSL context failed", e.fillInStackTrace());
            }
            connectOptions.setSocketFactory(ctx.getSocketFactory());
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

    public Profile setSSLSecured(boolean flag) {
        this.sslSecured = flag;
        return this;
    }

    public boolean getSSLSecured() {
        return this.sslSecured;
    }

    public String getJksPath() {
        return jksPath;
    }

    public Profile setJksPath(String jksPath) {
        this.jksPath = jksPath;
        return this;
    }

    public char[] getJksPwd() {
        return jksPwd;
    }

    public Profile setJksPwd(char[] jksPwd) {
        this.jksPwd = jksPwd;
        return this;
    }

    public String getJksAlgorithm() {
        return jksAlgorithm;
    }

    public Profile setJksAlgorithm(String jksAlgorithm) {
        this.jksAlgorithm = jksAlgorithm;
        return this;
    }

    public boolean isUseClientAuth() {
        return useClientAuth;
    }

    public Profile setUseClientAuth(boolean useClientAuth) {
        this.useClientAuth = useClientAuth;
        return this;
    }

    private SSLContext createContext(String path, char[] pwd, String algorithm, boolean useClientAuth) throws Exception {
        SSLContext context = SSLContext.getInstance("TLS");
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(getClass().getClassLoader().getResourceAsStream(path), pwd);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(algorithm);
        tmf.init(ks);
        if (useClientAuth) {
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(algorithm);
            kmf.init(ks, pwd);
            context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        } else {
            context.init(null, tmf.getTrustManagers(), null);
        }
        return context;
    }

}
