package com.envisioniot.enos.iot_mqtt_sdk.util;

import com.envisioniot.enos.iot_mqtt_sdk.core.MqttClient;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509ExtendedTrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.InputStream;
import java.net.Socket;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * @version $Id: EnvisionTrustManager.java, v 0.1 2017年3月10日 下午4:34:07
 *          lvjianwen Exp $
 */
public class EnvisionTrustManager extends X509ExtendedTrustManager {

    // 根证书认证
    private X509TrustManager rootTrusm;

    public EnvisionTrustManager() throws Exception {
        // CA根证书，可以从官网下载
        InputStream in = MqttClient.class.getResourceAsStream("/sChat.jks");
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        Certificate ca = null;
        try {
            ca = cf.generateCertificate(in);
        } catch (CertificateException e) {
            throw e;
        } finally {
            in.close();
        }
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        rootTrusm = (X509TrustManager) tmf.getTrustManagers()[0];

    }

    @Override
    public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        // 验证服务器证书合法性
        rootTrusm.checkServerTrusted(chain, authType);
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType, Socket socket) throws CertificateException {
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType, SSLEngine engine)
            throws CertificateException {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType, Socket socket) throws CertificateException {

        // 验证服务器证书合法性
        rootTrusm.checkServerTrusted(chain, authType);
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType, SSLEngine engine)
            throws CertificateException {
        // 验证服务器证书合法性
        rootTrusm.checkServerTrusted(chain, authType);
    }

}
