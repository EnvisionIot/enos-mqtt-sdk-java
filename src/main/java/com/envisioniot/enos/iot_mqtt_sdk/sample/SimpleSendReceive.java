package com.envisioniot.enos.iot_mqtt_sdk.sample;

import java.util.List;
import java.util.Random;

import com.envisioniot.enos.iot_mqtt_sdk.core.IConnectCallback;
import com.envisioniot.enos.iot_mqtt_sdk.core.MqttClient;
import com.envisioniot.enos.iot_mqtt_sdk.core.exception.EnvisionException;
import com.envisioniot.enos.iot_mqtt_sdk.core.msg.IMessageHandler;
import com.envisioniot.enos.iot_mqtt_sdk.message.downstream.device.SubDeviceDisableCommand;
import com.envisioniot.enos.iot_mqtt_sdk.message.downstream.device.SubDeviceDisableReply;
import com.envisioniot.enos.iot_mqtt_sdk.message.downstream.device.SubDeviceEnableCommand;
import com.envisioniot.enos.iot_mqtt_sdk.message.downstream.tsl.*;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.ResponseCode;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.register.DeviceRegisterRequest;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.register.DeviceRegisterResponse;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.status.SubDeviceLoginRequest;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.status.SubDeviceLoginResponse;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.status.SubDeviceLogoutRequest;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.status.SubDeviceLogoutResponse;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.tag.*;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.topo.*;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.tsl.*;
import com.envisioniot.enos.iot_mqtt_sdk.util.Pair;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

/**
 * @author zhensheng.cai
 * @date 2018/8/3.
 */
public class SimpleSendReceive {
    public static final String local = "tcp://localhost:11883";
    public static final String alpha = "tcp://10.24.10.56:11883";
    public static final String beta = "tcp://10.27.20.142:11883";
    public static final String prd = "tcp://10.24.8.76:11883";

    private static final String localSSL = "ssl://localhost:18883";
    private static final String alphaSSL = "ssl://10.24.10.56:18883";
    private static final String betaSSL = "ssl://10.24.101.51:18883";


    //alpha环境网关设备三元组
//	private static final String productKey = "invu9zyT";
//	public static final String deviceKey = "m7plCgtarp";
//	public static final String deviceSecret = "t3O5bRTfTYJ9UMS2wCrb";
//
//	//alpha环境子设备三元组
//	public static final String subProductKey = "ybuO63Oe";
//	public static final String subDeviceKey = "96Iy2aWmv7";
//	public static final String subDeviceSecret = "HUxm8Vcm7sod0v6XV8I3";

	/*alpha*/



    //beta
    // json device
    public static final String productKey = "dZjlcsgY";
    public static final String deviceKey = "zscai_test";
    public static final String deviceSecret = "WyKN6UEG5VQ2dKm7SuLV";

    // 透传 device
    public static final String parserProductKey = "WwVF5nKj";
    public static final String parserDevicekey = "device1";
    public static final String parserDeviceSecret = "mOpcSKsxSQsvr0nUHUiK";
    public static final String subProductKey = "Cb5bjujA";
    public static final String subDeviceKey = "zscai_sub_device";
    public static final String subDeviceSecret = "6F3ssoWPBAxTfMeKD71A";

//	private static final String productKey = "E8Fw4uiX";
//	public static final String deviceKey = "zscai-test-device2";
//	public static final String deviceSecret = "ivCESTLGx5nejNQg2EkR";




//	private static final String productKey = "NyDmJcbZ";
//	public static final String deviceKey = "xCPLxZtLKg";
//	public static final String deviceSecret = "0sfmTw2c9gY5JcopMrvd";
//
//	public static final String subProductKey = "muB7helV";
//	public static final String subDeviceKey = "UKaQFBAemf";
//	public static final String subDeviceSecret = "MEwVRDFptW6YctnS2GlF";


    private static MqttClient client;

    private static volatile boolean subDeviceLogined = false;


    public static void main(String[] args) throws Exception {

//		initSSLConnection();
//		disconnect();

        initWithCallback();

//        updateTag();
//        updateSubDeviceTag();
//        deleteTag();
//        deleteSubDeviceTag();
//        queryTag();
//
//        addTopo();
//        getTop    o();
//        postEvent();
////        subDeviceLogin();
//        postSubEvent();
//        subDeviceRegister();
//        getTslTemplete();
//        getSubTslTemplate();
////		subdeviceLogout();
////		deleteTopo();
        updateAttribute();
        queryAttribute();

//
        measurepointSetHandler();
        handleServiceInvocation();
        handleSubDeviceNotification();

//        fastpostMeasurepoint();

//         alwaysPostSubMeasurepoint();
//        alwaysPostMeasurepoint();
//        downRawHandler();
//        postUpRaw();

    }


    public static void alwaysPostMeasurepoint() throws Exception {
        while (true) {
//			postSubMeasurepoint();
            long ts = System.currentTimeMillis();
            postSyncMeasurepoint();
//			postSyncMeasurepoint();
            System.out.println(client.isConnected() + " post  cost " + (System.currentTimeMillis() - ts) + " millis");


            Thread.sleep(1000);
        }
    }

    public static void alwaysPostSubMeasurepoint() throws Exception {
        while (true) {
//			postSubMeasurepoint();
            if (subDeviceLogined) {
                long ts = System.currentTimeMillis();
                postSubMeasurepoint();
//			postSyncMeasurepoint();
                System.out.println(client.isConnected() + " sub device  post  cost " + (System.currentTimeMillis() - ts) + " millis");

            }
            Thread.sleep(1000);
        }
    }

    public static void initWithCallback() {
        System.out.println("start connect with callback ... ");
        try {
            client = new MqttClient(beta, productKey, deviceKey, deviceSecret); // json device

//            client = new MqttClient(local, parserProductKey, parserDevicekey, parserDeviceSecret); // 透传device
            client.getProfile().setConnectionTimeout(60).setAutoReconnect(false);
            client.connect(new IConnectCallback() {
                @Override
                public void onConnectSuccess() {
                    subDeviceLogin();
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

    public static void initSSLConnection() {
        System.out.println("start connect with callback ... ");
        try {
            client = new MqttClient(betaSSL, productKey, deviceKey, deviceSecret);
            client.getProfile().setConnectionTimeout(10).setSSLSecured(true);
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


    public static void updateAttribute(){
//        System.out.println("start update attribute (wrong)...");
//        AttributeUpdateRequest request = AttributeUpdateRequest.builder()
//                .addAttribute("attribute1", "newvalue fixed" )
//                .build();
//        try {
//            AttributeUpdateResponse rsp = client.publish(request);
//            System.out.println("--> " + rsp);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        System.out.println("start update attribute...");
        AttributeUpdateRequest request2 = AttributeUpdateRequest.builder()
                .addAttribute("attribute1", 1)
                .addAttribute("attribute3", Lists.newArrayList(random.nextFloat()* 2 , random.nextFloat()* 3 ))
                .addAttribute("attribute2" , "hello" + random.nextInt(100))
                .addAttribute("attribute4" , "email@emai.com")
                .build();
        try {
            AttributeUpdateResponse rsp2 = client.publish(request2);
            System.out.println("--> " + rsp2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void queryAttribute(){
        System.out.println("start query attribute...");
        AttributeQueryRequest request = AttributeQueryRequest.builder()
                .addAttribute("attribute1").queryAll().build();
        try {
            AttributeQueryResponse rsp = client.publish(request);
            System.out.println("--> " + rsp);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void updateTag(){

        TagUpdateRequest request = TagUpdateRequest.builder()
                .addTag("test_tag",
                        "new_tag" + new Random().nextInt(100))
//                .addTag("test_tag2",
//                        "new_tag" + new Random().nextInt(100))
                .build();
        request.setMessageId("test" + new Random().nextInt(1000));
        System.out.println("start update tag ... " + request.getMessageId());

        try {
            TagUpdateResponse rsp = client.publish(request);
            System.out.println("--> " + rsp);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void updateSubDeviceTag(){
        System.out.println("start update tag... ");
        TagUpdateRequest request = TagUpdateRequest.builder()
                .setProductKey(subProductKey)
                .setDeviceKey(subDeviceKey)
                .addTag("test_tag",
                        "new_tag" + new Random().nextInt(100))
                .addTag("test_tag2",
                        "new_tag" + new Random().nextInt(100)).build();

        try {
            TagUpdateResponse rsp = client.publish(request);
            System.out.println("--> " + rsp);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void queryTag(){
        System.out.println("start query tag");
        TagQueryRequest request = TagQueryRequest.builder()
                .queryAll().build();

        try {
            TagQueryResponse rsp = client.publish(request);
            System.out.println("--> " + rsp);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteTag(){
        System.out.println("start delete tag...");
        TagDeleteRequest request = TagDeleteRequest.builder().addTagKey("test_tag").build();
        try {
            TagDeleteResponse rsp = client.publish(request);
            System.out.println("-->" + rsp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteSubDeviceTag(){
        System.out.println("start delete tag...");
        TagDeleteRequest request = TagDeleteRequest.builder()
                .setProductKey(subProductKey)
                .setDeviceKey(subDeviceKey)
                .addTagKey("test_tag").build();

        try {
            TagDeleteResponse rsp = client.publish(request);
            System.out.println("-->" + rsp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void subDeviceRegister() {
        System.out.println("start register register sub-device , current status : " + client.isConnected());
        DeviceRegisterRequest request = DeviceRegisterRequest.builder()
                .addSubRegisterInfo(subProductKey, "zscai-sub-device-1", "zscai-sub-device-1", "zscai-sub-device-1")
                .addSubRegisterInfo(subProductKey, "zscai-sub-device-2", "zscai-sub-device-2", "zscai-sub-device-2")
                .addSubRegisterInfo(subProductKey, "zscai-sub-device-3", "zscai-sub-device-3", "zscai-sub-device-3")
                .build();
//		request.setRegProductKey("eb27piAg");
        DeviceRegisterResponse rsp = null;
        try {

            rsp = client.publish(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("--->" + rsp);
    }

    public static void subDeviceLogin() {
        SubDeviceLoginRequest request = SubDeviceLoginRequest.builder()
                .setSubDeviceInfo(subProductKey, subDeviceKey, subDeviceSecret).build();
        SubDeviceLoginResponse rsp = null;

        request.setMessageId("test"+ new Random().nextInt(1000));
        System.out.println("start login sub-device "+ request.getQos()+"+"+request.getMessageId() +" , current status : " + client.isConnected());

        try {
            rsp = client.publish(request);
            System.out.println("sub device login success ");

            if (rsp.getCode() == ResponseCode.SUCCESS) {
                subDeviceLogined = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("sub device login --> " + rsp);
    }

    public static void subdeviceLogout() throws InterruptedException {
        System.out.println("start logout sub device...");
        SubDeviceLogoutRequest request = SubDeviceLogoutRequest.builder()
                .setSubProductKey(subProductKey)
                .setSubDeviceKey(subDeviceKey).build();
        SubDeviceLogoutResponse rsp = null;
        try {
            rsp = client.publish(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(rsp);

//		System.out.println("Assert sub device logout ,post subdevice event");
//		Thread.sleep(3000);
//		postSubEvent();

    }


    public static void addTopo() throws Exception {
        System.out.println("start add topo ...");
        TopoAddRequest request = TopoAddRequest.builder().
                addSubDevice(new SubDeviceInfo(subProductKey, subDeviceKey, subDeviceSecret)).build();
        TopoAddResponse rsp = client.publish(request);
        System.out.println("-->" + rsp);
        getTopo();
//		subDeviceLogin();
//		subdeviceLogout();

    }

    public static void deleteTopo() throws Exception {
        System.out.println("start delete topo...");
        TopoDeleteRequest request = TopoDeleteRequest.builder()
                .setSubDevices(Lists.newArrayList(Pair.makePair(subProductKey, subDeviceKey))).build();
        TopoDeleteResponse rsp = client.publish(request);
        System.out.println("-->" + rsp);
        getTopo();
//		subDeviceLogin();
    }


    public static void getTopo() throws Exception {
        System.out.println("start get topo...");
        TopoGetRequest request = TopoGetRequest.builder().build();

        TopoGetResponse rsp = client.publish(request);
        System.out.println("-->" + rsp);

    }


    /*

    BaseMqttResponse{id='1', code=200, data={tslModelId=xfgBHRXU, tslAttributeMap={}, tslMeasurepointMap={}, tslServiceMap={}, tslEventMap={}, tag={tagMap={}}, allowAdditionalAttribute=false, inheritedAttributeIds=[], inheritedMeasurepointIds=[], inheritedServiceIds=[], inheritedEventIds=[]}, message='null', method='null'}
    */
    public static void getTslTemplete() {
        System.out.println("start get tsl template... ");
        TslTemplateGetRequest request = TslTemplateGetRequest.builder().build();
        TslTemplateGetResponse rsp = null;
        try {
            rsp = client.publish(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("-->" + rsp);
    }

    public static void getSubTslTemplate() {
        System.out.println("start get sub device tsl template... ");
        TslTemplateGetRequest request = TslTemplateGetRequest.builder().setProductKey(subProductKey)
                .setDeviceKey(subDeviceKey).build();
        TslTemplateGetResponse rsp = null;
        try {
            rsp = client.publish(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("-->" + rsp);
    }


    public static void postEvent() throws InterruptedException {
        System.out.println("start post event ");
        EventPostRequest postRequest = EventPostRequest.builder()
                .setEventIdentifier("PowerTooHigh")
                .addValue("PowerAlarm", 60)
                .build();
        try {
            /*
			 * 当前版本Post消息没有对应的reply
			 */
            client.fastPublish(postRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("post finish");

    }


    public static void postMeasurepoint() {
        Random random = new Random();
        System.out.println("start post measurepoint ...");
        MeasurepointPostRequest request = MeasurepointPostRequest.builder()
                .addMeasurePoint("point1", random.nextInt(100)).build();
//				.addMeasurePoint("p2", "{'value':123.4,  quality:2}").build();
        try {
            client.fastPublish(request);

//			System.out.println("-->" + rsp);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void postSubMeasurepoint() {
        Random random = new Random();
        System.out.println("start post sub device measurepoint ...");
        MeasurepointPostRequest request = MeasurepointPostRequest.builder()
                .setProductKey(subProductKey).setDeviceKey(subDeviceKey)
                .addMeasurePoint("point1", random.nextInt(100)).build();
//				.addMeasurePoint("p2", "{'value':123.4,  quality:2}").build();
        try {
            MeasurepointPostResponse rsp = client.publish(request);

            System.out.println("-->" + rsp);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void postSyncMeasurepoint() {
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

    public static void fastpostMeasurepoint() {
        Random random = new Random();
        System.out.println("start post measurepoint ...");
        MeasurepointPostRequest request = MeasurepointPostRequest.builder()
                .addMeasurePoint("point1", random.nextInt(100)).build();
        try {
           client.fastPublish(request);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public static void postSubEvent() {
        System.out.println("start post sub device event ");
        EventPostRequest postRequest = EventPostRequest.builder()
                .setEventIdentifier("PowerTooHigh")
                .addValue("PowerAlarm", 60)
                .setDeviceKey(subDeviceKey)
                .setProductKey(subProductKey)
                .build();
        try {
            /**
             * 当前版本Post消息没有对应的reply
             */
            client.fastPublish(postRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("post finish");
    }

    public static void handleSubDeviceNotification(){

        client.setArrivedMsgHandler(SubDeviceDisableCommand.class, (arrivedMessage, argList) -> {
            System.out.println("rcv subDeviceDisableCommand");
            System.out.println(arrivedMessage);
            return null;
        });

        client.setArrivedMsgHandler(SubDeviceEnableCommand.class, (arrivedMessage, argList) -> {
            System.out.println("rcv subDeviceEnableCommand");
            System.out.println(arrivedMessage);
            return null;
        });
    }

    public static void handleServiceInvocation() {
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



    public static void measurepointSetHandler() {
        client.setArrivedMsgHandler(MeasurepointSetCommand.class, (arrivedMessage, argList) -> {
            System.out.println(arrivedMessage);
//            return null;
            return MeasurepointSetReply.builder().build();
        });

        client.setArrivedMsgHandler(SubDeviceDisableCommand.class, (command, argList) -> {
            // disable device and ret 201 reply
            System.out.println("rcv disable command -> " + command);
            return SubDeviceDisableReply.builder().setCode(201).build();
        });
    }

    private static Random random = new Random();
    private static int idInc = 20;

    private static byte[] getPayload()
    {
        byte[] array = new byte[100];
        int index = 0;
        // 云端脚本定义了0x01对应的method=thing.model.measurepoint.post
        byte method = 0x01;
        array[index++] = method; // method;
        int id = idInc++;
        System.out.println("id: " + id);
        array[index++] = (byte) ((id >> 24) & 0xff);
        array[index++] = (byte) ((id >> 16) & 0xff);
        array[index++] = (byte) ((id >> 8) & 0xff);
        array[index++] = (byte) (id & 0xff);
        int prop_int16 = random.nextInt(1000);
        array[index++] = (byte) ((prop_int16 >> 8) & 0xff);
        array[index++] = (byte) (prop_int16 & 0xff);
        int prop_bool = random.nextInt(100);
        array[index++] = (byte) (prop_bool & 0xff);
        float prop_float = random.nextFloat();
        int fint = Float.floatToIntBits(prop_float);
        array[index++] = (byte) ((fint >> 24) & 0xff);
        array[index++] = (byte) ((fint >> 16) & 0xff);
        array[index++] = (byte) ((fint >> 8) & 0xff);
        array[index++] = (byte) (fint & 0xff);
        byte[] payload = new byte[index];
        System.arraycopy(array, 0, payload, 0, index);
        return payload;
    }

    public static void postUpRaw()
    {
        try
        {
            while(true)
            {
                byte[] payload = getPayload();
                ModelUpRawRequest request = ModelUpRawRequest.builder().setPayload(payload).build();
                System.out.println("payload: " + bytesToHexStr(payload));
                ModelUpRawResponse rsp = client.publish(request);
                Thread.sleep(1000 * 5);
                System.out.println("raw -->" + rsp);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void downRawHandler()
    {
        client.setArrivedMsgHandler(ModelDownRawCommand.class, (IMessageHandler<ModelDownRawCommand, ModelDownRawReply>) (command, argList)->{
            System.out.println("modelDownRawCommand");
            byte[] payload = command.getPayload();
            System.out.println("payload: " + bytesToHexStr(payload));
            int index = 0;
            int id = payload[index++];
            id = (id << 8) + payload[index++];
            id = (id << 8) + payload[index++];
            id = (id << 8) + payload[index++];
            System.out.println("id: " + id);
            byte methodCode = payload[index++];
            String method = "";
            switch (methodCode)
            {
                case 0x03:
                    method = "thing.service.measurepoint.set";
                    break;
                case 0x04:
                    method = "thing.service.measurepoint.get";
                    break;
                case 0x05:
                    method = "thing.service.switch"; //  'switch' is identifier of service
            }
            System.out.println("method: " + method);
            ModelDownRawReply reply = new ModelDownRawReply();
            byte[] replyPayload = new byte[6];
            int replyIndex = 0;
            replyPayload[replyIndex++] = methodCode;
            System.arraycopy(payload, 0, replyPayload, replyIndex, 4);
            replyIndex += 4;
            replyPayload[replyIndex++] = (byte) (200 & 0xff);
            reply.setPayload(replyPayload);
            return reply;
        });

    }

    public static void disconnect() throws EnvisionException {
        client.disconnect();
        System.out.println("disconnect success");
    }


    private static final char[] HEX_CHAR = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    public static String ubytesToHexStr(int[] bytes)
    {
        if (bytes == null || bytes.length == 0)
        {
            return null;
        }
        char[] buf = new char[bytes.length * 2];
        int index = 0;
        for (int b : bytes)
        {
            buf[index++] = HEX_CHAR[b >>> 4 & 0xf];
            buf[index++] = HEX_CHAR[b & 0xf];
        }

        return "0x" + new String(buf);
    }

    public static String bytesToHexStr(byte[] bytes)
    {
        if (bytes == null || bytes.length == 0)
        {
            return null;
        }
        char[] buf = new char[bytes.length * 2];
        int index = 0;
        for (byte b : bytes)
        {
            buf[index++] = HEX_CHAR[b >>> 4 & 0xf];
            buf[index++] = HEX_CHAR[b & 0xf];
        }
        return "0x" + new String(buf);
    }


}
