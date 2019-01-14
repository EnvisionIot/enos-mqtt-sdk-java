package com.envisioniot.enos.iot_mqtt_sdk.core.profile;

import com.envisioniot.enos.iot_mqtt_sdk.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author zhensheng.cai
 * @date 2019/1/3.
 */
public class FileProfile extends AbstractProfile {

    private Logger logger = LoggerFactory.getLogger(FileProfile.class);
    private String filePath;
    private Properties properties;


    public FileProfile() {
        this(".config");
    }


    public FileProfile(String filePath) {
        super();
        this.filePath = filePath;
        try {
            config();
        } catch (IOException e) {
            logger.error("load file [{}] profile failed , ", filePath, e);
        }
    }


    public Properties getProperties() {
        return properties;
    }

    private void config() throws IOException {
        this.properties = loadProperties(this.filePath);
//        if (StringUtil.isEmpty(p_regionURL) || StringUtil.isEmpty(p_productKey) || StringUtil.isEmpty(p_deviceKey)
//                || (StringUtil.isEmpty(p_deviceSecret) && StringUtil.isEmpty(p_productSecret))) {
//            throw new IllegalArgumentException("file config missing mandatory fields, please check config file");
//        }
        super.serverUrl = properties.getProperty("serverUrl");
        super.productKey = properties.getProperty("productKey");
        super.productSecret = properties.getProperty("productSecret");
        super.deviceKey = properties.getProperty("deviceKey");
        super.deviceSecret = properties.getProperty("deviceSecret");
        super.setExecutorService(createDefaultExecutorService());
        setOptionalConfig(properties);
    }

    private void setOptionalConfig(Properties properties) {
        if (properties.contains("keepAlive")) {
            try {
                this.setKeepAlive(Integer.valueOf(properties.getProperty("keepAlive")));
            } catch (Exception e) {
                logger.warn("config filed [{}] in config file invalid , please check", "keepAlive");
            }
        }
        if (properties.contains("connectionTimeout")) {
            try {
                this.setConnectionTimeout(Integer.valueOf(properties.getProperty("connectionTimeout")));
            } catch (Exception e) {
                logger.warn("config filed [{}] in config file invalid , please check", "connectionTimeout");
            }
        }
        if (properties.contains("operationTimeout")) {
            try {
                this.setTimeToWait(Integer.valueOf(properties.getProperty("operationTimeout")));
            } catch (Exception e) {
                logger.warn("config filed [{}] in config file invalid , please check", "operationTimeout");
            }
        }
        if (properties.contains("maxInFlight")) {
            try {
                this.setMaxInFlight(Integer.valueOf(properties.getProperty("maxInFlight")));
            } catch (Exception e) {
                logger.warn("config filed [{}] in config file invalid , please check", "maxInFlight");
            }
        }
        if (properties.contains("autoReconnect")) {
            try {
                this.setAutoReconnect(Boolean.valueOf(properties.getProperty("autoReconnect")));
            } catch (Exception e) {
                logger.warn("config filed [{}] in config file invalid , please check", "autoReconnect");
            }
        }
        if (properties.contains("sslJksPath") && properties.contains("sslPassword")) {
            try {
                if (StringUtil.isNotEmpty(properties.getProperty("sslJksPath"))) {
                    this.setSSLJksPath(properties.getProperty("sslJksPath"), properties.getProperty("sslPassword"));
                    this.setSSLSecured(true);
                }
            } catch (Exception e) {
                logger.warn("config filed sslJksPath/sslPassword in config file invalid , please check");
            }
        }
        if (properties.contains("sslAlgorithm")) {
            try {
                this.setSSLAlgorithm(properties.getProperty("sslAlgorithm"));
            } catch (Exception e) {
                logger.warn("config filed [{}] in config file invalid , please check", "sslAlgorithm");
            }
        }
    }


    /**
     * @param filePath
     * @return
     */
    private Properties loadProperties(String filePath) throws IOException {

        FileInputStream fileInStream = new FileInputStream(filePath);
        try {
            Properties properties = new Properties();
            properties.load(fileInStream);
            return properties;
        } finally {
            fileInStream.close();
        }
    }


    public void persistent(String filePath) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        try {
            this.properties.store(fileOutputStream, "store properties by mqtt sdk");
        } finally {
            fileOutputStream.close();
        }
    }

    public void persistent() throws IOException {
        this.persistent(this.filePath);
    }

    public static void main(String[] args) {
        FileProfile profile = new FileProfile();
        profile.getProperties().setProperty("productSecret", "abc");
        try {
            profile.persistent();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void reload() {
        try {
            config();
        } catch (IOException e) {
            logger.error("load file [{}] profile failed , ", filePath, e);
        }
    }
}
