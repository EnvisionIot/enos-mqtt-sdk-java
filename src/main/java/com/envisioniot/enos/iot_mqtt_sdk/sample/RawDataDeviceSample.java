package com.envisioniot.enos.iot_mqtt_sdk.sample;

import com.envisioniot.enos.iot_mqtt_sdk.core.IConnectCallback;
import com.envisioniot.enos.iot_mqtt_sdk.core.MqttClient;
import com.envisioniot.enos.iot_mqtt_sdk.core.profile.DefaultProfile;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.tsl.ModelUpRawRequest;

/**
 * @author zhensheng.cai
 * @date 2019/1/8.
 */
public class RawDataDeviceSample {

    static String productKey = "MXjDK5GC";
    static String deviceKey = "1FlzuBCC2f";
    static String deviceSecret = "cOVXeqarUKAAj6LudWUF";

    //ALPHA
    static String brokerUrl = "tcp://10.27.21.6:11883";

    static MqttClient client;

    public static void main(String[] args) throws Exception {
        client = new MqttClient(new DefaultProfile(brokerUrl, productKey, deviceKey, deviceSecret));
        client.connect(new IConnectCallback() {
            @Override
            public void onConnectSuccess() {
                System.out.println("connect success");
                ModelUpRawRequest.Builder builer = new ModelUpRawRequest.Builder();
                builer.setProductKey(productKey).setDeviceKey(deviceKey);
                builer.setPayload(hexStrToBytes("0x01010203040200000167a673ea2d"));
                try {
                    client.fastPublish(builer.build());
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
    }

    public static byte[] hexStrToBytes(String hexStr) {
        if (hexStr == null || hexStr.trim().equals("")) {
            return new byte[0];
        }
        if (hexStr.startsWith("0x")) {
            hexStr = hexStr.substring(2);
        }

        byte[] bytes = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            String subStr = hexStr.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }

        return bytes;
    }
}
