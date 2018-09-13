package com.envisioniot.enos.iot_mqtt_sdk.sample;


import com.google.gson.Gson;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MqttPub {
    private static final char[] PASSWD = "cNetty".toCharArray();
    private static final String ALGORITHM = "SunX509";
    private static final String jks = "cChat.jks";

    private static Gson gson = new Gson();
    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private static SSLContext createContext() throws Exception {
        SSLContext context = SSLContext.getInstance("TLS");
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(new File(jks)), PASSWD);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(ALGORITHM);
        tmf.init(ks);
        context.init(null, tmf.getTrustManagers(), null);
        return context;
    }


    public static void main(String[] args) throws Exception {
        final String broker = "ssl://developer.envisioncn.com:18883";

        final String topic = "topicname01";
        new Thread(() ->
        {
			try {
				SSLContext ctx = createContext();
// 其中broker表示主机名；“paho‐java‐client‐3”表示用户ID
				MqttClient sampleClient = new MqttClient(broker, "paho‐java‐ client‐3", new MqttDefaultFilePersistence());
				MqttConnectOptions connOpts = new MqttConnectOptions();
				connOpts.setCleanSession(false);
// username为设备名称；password为设备密钥
				connOpts.setUserName("device");
				connOpts.setPassword("RlZN8H/+4mt/ePd5SD4LFixctwjWcnXIeLVU42aNWRlAoRYjVoO2TPMuAQE=".toCharArray());
				connOpts.setSocketFactory(ctx.getSocketFactory());
				sampleClient.connect(connOpts);

				System.out.println(sampleClient.isConnected());

//                    for (int i = 0; i < 10; i++) {
//                        String msg = String
//                                .format("{\"object\": \"1958f6c92bc00000\"," +
//                                                "\"timestamp\":%d, \"METER3X.UA\":\"50.0\", \"DEVICE_CONN\":\"%d\"}",
//                                        System.currentTimeMillis(), i);
//                        MqttMessage message = new MqttMessage(msg.getBytes());
//                        message.setQos(1);
//// message.setRetained(true);
//                        sampleClient.publish("topicname01", message);
//                    }


				//组织消息发送消息。
				Map<String, Object> msg = new HashMap<>();
				long ts = System.currentTimeMillis();

				//mdmId_xxx，实际使用时请替换为设备的mdmId
				msg.put("object", "mdmId_12345");

				//ts, 实际使用时请替换为设备数据时间（utc毫秒数）
				msg.put("timestamp", ts);

				//pointname_xxx，实际使用时请替换为要发送的点名
				//100， 实际使用时请替换为该点对应的值
				msg.put("pointname", "INV.ActivePower");

				//下面为pointname_xxx点设置attr
				Map<String, Object> pointAttrMap = new HashMap<>();
				pointAttrMap.put("timestamp", ts);
				Map<String, Map<String, Object>> attrsMap = new HashMap<>();
				attrsMap.put("INV.ActivePower", pointAttrMap);
				//_attributes_为系统预留字段，必须这么写
				msg.put("_attributes_", attrsMap);

				MqttMessage mqttMessage = new MqttMessage(gson.toJson(msg).getBytes());
				mqttMessage.setQos(1);
				try {
					//订阅topic的消息，如果有client往该topic发送消息，client会收到并在回调方法中打印该消息
					sampleClient.subscribe(topic);
					//发送消息到该topic
					sampleClient.publish(topic, mqttMessage);
				} catch (MqttException e) {
					e.printStackTrace();
				}

				System.out.println("send => " + msg + " at " + df.format(ts));


			} catch (Exception me) {
				me.printStackTrace();
			}
		}).start();
        //TimeUnit.SECONDS.sleep(2);
    }
}