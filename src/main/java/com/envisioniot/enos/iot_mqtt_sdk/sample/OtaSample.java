package com.envisioniot.enos.iot_mqtt_sdk.sample;

import com.envisioniot.enos.iot_mqtt_sdk.core.IConnectCallback;
import com.envisioniot.enos.iot_mqtt_sdk.core.MqttClient;
import com.envisioniot.enos.iot_mqtt_sdk.core.exception.EnvisionException;
import com.envisioniot.enos.iot_mqtt_sdk.core.msg.IMessageHandler;
import com.envisioniot.enos.iot_mqtt_sdk.core.msg.IMqttDeliveryMessage;
import com.envisioniot.enos.iot_mqtt_sdk.core.profile.DefaultProfile;
import com.envisioniot.enos.iot_mqtt_sdk.message.downstream.ota.OtaUpgradeCommand;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.ota.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * device firmware over-the-air
 *
 * @author zhensheng.cai
 * @date 2019/1/3.
 */
public class OtaSample {

    static String productKey = "testPK";
    static String deviceKey = "testDK";
    static String deviceSecret = "testDS";

    //ALPHA
    static String brokerUrl = "tcp://{mqtt-broker-url}";

    static MqttClient client;

    public static void main(String[] args) throws Exception {
        client = new MqttClient(new DefaultProfile(brokerUrl, productKey, deviceKey, deviceSecret));
        initWithCallback(client);

        reportVersion("initVersion1");

        //        upgradeFirmwareByCloudPush();


        //        upgradeFirmwareByDeviceReq();
    }

    public static void upgradeFirmwareByCloudPush() {
        client.setArrivedMsgHandler(OtaUpgradeCommand.class, new IMessageHandler<OtaUpgradeCommand, IMqttDeliveryMessage>() {
            @Override
            public IMqttDeliveryMessage onMessage(OtaUpgradeCommand otaUpgradeCommand, List<String> list) throws Exception {
                System.out.println("receive command: " + otaUpgradeCommand);

                Firmware firmware = otaUpgradeCommand.getFirmwareInfo();

                //TODO: download firmware from firmware.fileUrl

                //mock reporting progress
                reportUpgradeProgress("20", "20");
                TimeUnit.SECONDS.sleep(2);

                reportUpgradeProgress("25", "25");
                TimeUnit.SECONDS.sleep(20);

                reportUpgradeProgress("80", "80");
                TimeUnit.SECONDS.sleep(20);

                //firmware upgrade success, report new version
                reportVersion(otaUpgradeCommand.getFirmwareInfo().version);

                return null;
            }
        });
    }

    public static void upgradeFirmwareByDeviceReq() throws Exception {
        List<Firmware> firmwareList = getFirmwaresFromCloud();
        String version = null;
        for (Firmware firmware : firmwareList) {
            version = firmware.version;
            StringBuffer sb = new StringBuffer();
            sb.append("Firmware=>[");
            sb.append("version=" + firmware.version);
            sb.append("signMethod=" + firmware.signMethod);
            sb.append("sign=" + firmware.sign);
            sb.append("fileUrl=" + firmware.fileUrl);
            sb.append("fileSize=" + firmware.fileSize);
            sb.append("]");
            System.out.println(sb.toString());
        }
        if (version != null) {
            reportUpgradeProgress("20", "20");
            TimeUnit.SECONDS.sleep(10);
            reportUpgradeProgress("80", "80");
            TimeUnit.SECONDS.sleep(20);
            reportVersion(version);
        }
    }

    public static void reportVersion(String version) throws Exception {
        OtaVersionReportRequest.Builder builder = new OtaVersionReportRequest.Builder();
        builder.setProductKey(productKey).setDeviceKey(deviceKey).setVersion(version);
        OtaVersionReportRequest request = builder.build();
        System.out.println("send =>" + request.toString());
        client.fastPublish(builder.build());
    }

    private static void reportUpgradeProgress(String progress, String desc) throws Exception {
        OtaProgressReportRequest.Builder builder = new OtaProgressReportRequest.Builder();
        builder.setStep(progress).setDesc(desc);
        client.fastPublish(builder.build());
    }

    private static List<Firmware> getFirmwaresFromCloud() throws Exception {
        OtaGetVersionRequest.Builder builder = new OtaGetVersionRequest.Builder();
        builder.setProductKey(productKey).setDeviceKey(deviceKey);
        OtaGetVersionRequest request = builder.build();
        OtaGetVersionResponse response = client.publish(request);
        System.out.println("send getversion request =>" + request.toString());
        System.out.println("receive getversion response =>" + response.toString());
        return response.getFirmwareList();
    }

    private static void initWithCallback(MqttClient client) {
        System.out.println("start connect with callback ... ");
        try {
            client.connect(new IConnectCallback() {
                @Override
                public void onConnectSuccess() {
                    System.out.println("connect success");
                }

                @Override
                public void onConnectLost() {
                    System.out.println("onConnectLost");
                }

                @Override
                public void onConnectFailed(int reasonCode) {
                    System.out.println("onConnectFailed : " + reasonCode);
                }

            });
        } catch (EnvisionException e) {
            e.printStackTrace();
        }
    }

}

