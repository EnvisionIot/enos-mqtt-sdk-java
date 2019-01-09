package com.envisioniot.enos.iot_mqtt_sdk.sample;

import com.envisioniot.enos.iot_mqtt_sdk.core.IConnectCallback;
import com.envisioniot.enos.iot_mqtt_sdk.core.MqttClient;
import com.envisioniot.enos.iot_mqtt_sdk.core.exception.EnvisionException;
import com.envisioniot.enos.iot_mqtt_sdk.core.profile.FileProfile;
import com.envisioniot.enos.iot_mqtt_sdk.message.downstream.activate.DeviceActivateInfoCommand;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.tsl.MeasurepointPostRequest;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.tsl.MeasurepointPostResponse;

import java.util.List;
import java.util.Random;

/**
 * sample for dynamic activate device by productSecret
 *
 * @author zhensheng.cai
 * @date 2019/1/3.
 */
public class DynamicActivateSample {


    public static void dynamicActivateByFileProfile()  {

        MqttClient client = new MqttClient(new FileProfile());
        initWithCallback(client);
        while(true){
            postSyncMeasurepoint(client);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void dynamicActivateByDefaultProfile(){

    }

    public static void postSyncMeasurepoint(MqttClient client) {
        Random random = new Random();
        System.out.println("start post measurepoint ...");
        MeasurepointPostRequest request = MeasurepointPostRequest.builder()
                .addMeasurePoint("point1", random.nextInt(100)).build();
        try {
            MeasurepointPostResponse rsp = client.publish(request);
            System.out.println("-->" + rsp);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void initWithCallback(MqttClient client) {

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
            //e.printStackTrace();
        }
        System.out.println("connect result :" + client.isConnected());
    }

    public static void main(String[] args) {
        dynamicActivateByFileProfile();
    }


}

