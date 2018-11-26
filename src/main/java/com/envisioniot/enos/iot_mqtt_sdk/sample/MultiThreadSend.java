package com.envisioniot.enos.iot_mqtt_sdk.sample;

import com.envisioniot.enos.iot_mqtt_sdk.core.IConnectCallback;
import com.envisioniot.enos.iot_mqtt_sdk.core.MqttClient;
import com.envisioniot.enos.iot_mqtt_sdk.core.exception.EnvisionException;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.tsl.MeasurepointPostRequest;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.tsl.MeasurepointPostResponse;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author zhensheng.cai
 * @date 2018/11/14.
 */
public class MultiThreadSend {

    private static MqttClient client;

    public static final String region = SimpleSendReceive.alpha;

    private static  ExecutorService executor = Executors.newFixedThreadPool(10);

    private static AtomicLong counter = new AtomicLong();
    public static void main(String[] args) {
        initWithCallback();

        long begin = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            executor.execute(()->{
                while(true){
                    //tps : 50.76029449850161
                    postSyncMeasurepoint();
//                    try {
//                        Thread.sleep();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }

                }
            });
        }


        new Thread(() -> {
            while (true) {

                System.out.println("tps : " + (counter.get() * 1000.0) / (System.currentTimeMillis() - begin));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public static void postSyncMeasurepoint() {
        Random random = new Random();
        System.out.println("start post measurepoint ...");
        MeasurepointPostRequest request = MeasurepointPostRequest.builder()
                .addMeasurePoint("point1", random.nextInt(100)).build();
        request.setQos(1);

        try {
            MeasurepointPostResponse rsp = client.publish(request);
            System.out.println("-->" + rsp);
            counter.incrementAndGet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void initWithCallback() {
        System.out.println("start connect with callback ... ");
        try {
            client = new MqttClient(region,
                    SimpleSendReceive.productKey,
                    SimpleSendReceive.deviceKey,
                    SimpleSendReceive.deviceSecret); // json device

//            client = new MqttClient(local, parserProductKey, parserDevicekey, parserDeviceSecret); // 透传device
            client.getProfile().setConnectionTimeout(60);
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
}
