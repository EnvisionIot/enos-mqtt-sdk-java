package com.envisioniot.enos.iot_mqtt_sdk.sample;

import com.envisioniot.enos.iot_mqtt_sdk.core.IConnectCallback;
import com.envisioniot.enos.iot_mqtt_sdk.core.MqttClient;
import com.envisioniot.enos.iot_mqtt_sdk.core.exception.EnvisionException;
import com.envisioniot.enos.iot_mqtt_sdk.core.msg.IMessageHandler;
import com.envisioniot.enos.iot_mqtt_sdk.core.profile.DeviceCredential;
import com.envisioniot.enos.iot_mqtt_sdk.core.profile.FileProfile;
import com.envisioniot.enos.iot_mqtt_sdk.message.downstream.activate.DeviceActivateInfoCommand;
import com.envisioniot.enos.iot_mqtt_sdk.message.downstream.tsl.ServiceInvocationCommand;
import com.envisioniot.enos.iot_mqtt_sdk.message.downstream.tsl.ServiceInvocationReply;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.topo.SubDeviceInfo;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.topo.TopoAddRequest;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.topo.TopoAddResponse;
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
        handleServiceInvocation(client);
        initWithCallback(client);
//        addTopo(client);
//        while(true){
//            postSyncMeasurepoint(client);
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            if(!client.getProfile().getSubDevices().isEmpty()){
//                postSyncMeasurepoint(client, client.getProfile().getSubDevices().get(0));
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }

    }


    static String subProdcutKey = "ShmY4q9h";

    static String subDeviceKey = "zscai_sub_activate";

    static String subDeviceSecret = "KDe3rIeYID0eSSdixqR7";

    public static void addTopo(MqttClient client) {
        System.out.println("start add topo ...");
        TopoAddRequest request = TopoAddRequest.builder().
                addSubDevice(new SubDeviceInfo(subProdcutKey, subDeviceKey, subDeviceSecret)).build();
        TopoAddResponse rsp = null;
        try {
            rsp = client.publish(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("-->" + rsp);
//		subDeviceLogin();
//		subdeviceLogout();

    }

    public static void postSyncMeasurepoint(MqttClient client , DeviceCredential subDevice) {
        Random random = new Random();
        System.out.println("start post measurepoint ...");
        MeasurepointPostRequest request = MeasurepointPostRequest.builder()
                .setProductKey(subDevice.productKey).setDeviceKey(subDevice.deviceKey)
                .addMeasurePoint("point1", random.nextInt(100)).build();
        try {
            MeasurepointPostResponse rsp = client.publish(request);
            System.out.println("-->" + rsp);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void postSyncMeasurepoint(MqttClient client ) {
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


    public static void postSyncMeasurepointWithNtpTime(MqttClient client ) {
        Random random = new Random();
        System.out.println("start post measurepoint ...");
        MeasurepointPostRequest request = MeasurepointPostRequest.builder()
                .addMeasurePoint("point1", random.nextInt(100))
                .setTimestamp(client.getExtServiceFactory().getNtpService().getFixedTimestamp())
                .build();
        try {
            MeasurepointPostResponse rsp = client.publish(request);
            System.out.println("-->" + rsp);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void handleServiceInvocation(MqttClient client) {

        IMessageHandler<ServiceInvocationCommand, ServiceInvocationReply> handler = new IMessageHandler<ServiceInvocationCommand, ServiceInvocationReply>() {
            @Override
            public ServiceInvocationReply onMessage(ServiceInvocationCommand request, List<String> argList) throws Exception {
                System.out.println("rcvn async serevice invocation command" + request + " topic " + argList);
                return ServiceInvocationReply.builder()
//                        .setCode(2000)
//                        /**/.setMessage("user defined err msg")
//                        .addOutputData("pointA", "11")
                        .addOutputData("point1", 11)
                        .build();
            }

        };
        client.setArrivedMsgHandler(ServiceInvocationCommand.class, handler);
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

