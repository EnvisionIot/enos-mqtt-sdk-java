package com.envisioniot.enos.iot_mqtt_sdk.extension;

/**
 * @author zhensheng.cai
 * @date 2019/1/9.
 */
public class ExtServiceFactory {

    /*
     * lazy initialized ntp service
     */
    private static NtpService ntpService;

    public NtpService getNtpService() {
        if (ntpService == null) {
            synchronized (ExtServiceFactory.class) {
                if (ntpService == null) {
                    ntpService = new NtpService();
                }
            }
        }
        return ntpService;
    }

}
