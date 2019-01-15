package com.envisioniot.enos.iot_mqtt_sdk.core.profile;

import com.envisioniot.enos.iot_mqtt_sdk.util.GsonUtil;
import com.envisioniot.enos.iot_mqtt_sdk.util.StringUtil;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * @author zhensheng.cai
 * @date 2019/1/3.
 */
public class FileProfile extends BaseProfile {

    private Logger logger = LoggerFactory.getLogger(FileProfile.class);
    private String filePath;


    public FileProfile() {
        this(".config");
    }


    public FileProfile(String filePath) {
        super();
        this.filePath = filePath;
        try {
            tryConfig();
            setExecutorService(createDefaultExecutorService());
        } catch (IOException e) {
            logger.error("load file [{}] profile failed , ", filePath, e);
        }
    }


    public Config getConfig() {
        return super.config;
    }

    private void tryConfig() throws IOException {
        super.config = loadConfig(this.filePath);
    }

    private void setOptionalConfig(Config config) {
        if (config.getKeepAlive() != null) {
            this.setKeepAlive(config.getKeepAlive());
        }
        if (config.getConnectionTimeout() != null) {
            this.setConnectionTimeout(config.getConnectionTimeout());
        }
        if (config.getOperationTimeout() != null) {
            this.setTimeToWait(config.getConnectionTimeout());
        }

        if (config.getMaxInFlight() != null) {
            this.setMaxInFlight(config.getMaxInFlight());
        }
        if (config.getAutoReconnect() != null) {
            this.setAutoReconnect(config.getAutoReconnect());
        }
        if (StringUtil.isNotEmpty(config.getSslJksPath())) {
            this.setSSLJksPath(config.getSslJksPath(), config.getSslPassword() == null ? "" : config.getSslPassword());
            this.setSSLSecured(true);
        }
        if (StringUtil.isNotEmpty(config.getSslAlgorithm())) {
            this.setSSLAlgorithm(config.getSslAlgorithm());
        }
    }


    /**
     * @param filePath
     * @return
     */
    private Config loadConfig(String filePath) throws IOException {
        String json = Files.toString(new File(filePath), Charsets.UTF_8);
        return GsonUtil.fromJson(json, Config.class);
    }


    public void persistent(String filePath) throws IOException {
        this.config.store(filePath, "store config by mqtt sdk");
    }

    public void persistent() throws IOException {
        this.persistent(this.filePath);
    }

    public static void main(String[] args) {
        FileProfile profile = new FileProfile();
        profile.getConfig().setDeviceSecret("abc");
        try {
            profile.persistent();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void reload() {
        try {
            tryConfig();
        } catch (IOException e) {
            logger.error("load file [{}] profile failed , ", filePath, e);
        }
    }
}
