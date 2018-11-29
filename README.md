# Using EnOS IoT MQTT SDK for Java

This article instructs how to prepare your development environment to use the *EnOS IoT MQTT SDK for Java*.

* [Installing Java JDK SE](#installjava)
* [Installing Maven](#installmaven)
* [Obtaining EnOS IoT MQTT SDK for Java](#installiot)
	* [Include dependency in Maven project](#installiotmaven)
	* [Building from source](#installiotsource)
* [Feature list](#featurelist)
* [API reference](#apiref)
* [Sample code](#samplecode)

<a name="installjava"></a>
## Installing Java JDK SE
To use the EnOS IoT MQTT SDK for Java, you will need **Java SE 8**.

<a name="installmaven"></a>
## Installing Maven
EnOS IoT MQTT SDK for Java, we recommend you to use **_Maven 3_**.

<a name="installiot"></a>
## Obtaining EnOS IoT MQTT SDK for Java

You can obtain the EnOS IoT MQTT SDK through the following methods:
* Include the project as a dependency in your Maven project.
* Download the source code by cloning this repo and build on your machine

<a name="installiotmaven"></a>
### Get EnOS IoT MQTT SDK for Java from Maven (as a dependency)
_This is the recommended method of including the EnOS IoT SDKs in your project._

* Navigate to http://search.maven.org, search for **com.envisioniot.enos** and take note of the latest version number (or the version number of whichever version of the sdk you desire to use).
* In your main pom.xml file, add the EnOS IoT MQTT SDK as a dependency as follows:
```xml
	<dependency>
	  <groupId>com.envisioniot.enos</groupId>
	  <artifactId>iot_mqtt_sdk</artifactId>
	  <version>0.0.1-SNAPSHOT</version>
	  <!--You might need to change the version number as you need.-->
	</dependency>
```

<a name="installiotsource"></a>
### Build EnOS IoT MQTT SDK for Java from the source code in this repo
* Get a copy of the **EnOS IoT SDK for Java** from master branch of the GitHub (current repo). You should fetch a copy of the source from the **master** branch of the GitHub repository: <https://github.com/EnvisionIot/enos-iot-mqtt-java-sdk>
```
	git clone https://github.com/EnvisionIot/enos-iot-mqtt-java-sdk.git
```
* When you have obtained a copy of the source, you can build the SDKs for Java.

<a name="featurelist"></a>
## Key features

The EnOS IoT MQTT SDK supports the following functions:

- 支持子设备的身份注册
- 支持网关设备的拓扑增删改查
- 支持子设备的上下线能力
- 支持设备标签的上报删除
- 支持设备的测点上报
- 支持设备的事件上报
- 支持设备的透传报文上送
- 支持设备置数（测点设置）及测点主动获取的能力
- 支持设备服务的触发（俗称的控）
- 支持设备的启用，禁用，删除的消息通知
- 支持设备的通用控制指令（RRPC）

<a name="apiref"></a>
## API reference
Link to Java SDK reference in EnOS Help Center.

<a name="samplecode"></a>
## Sample code

The following sample codes instruct how to use the SDK.

### 连接服务器

```
MqttClient client = new MqttClient(prd, productKey, deviceKey, deviceSecret);
client.connect(new IConnectCallback()
{
	@Override
	public void onConnectSuccess()
	{
		System.out.println("onConnectSuccess");
	}

	@Override
	 public void onConnectLost()
	{
		System.out.println("onConnectLost");
	}

	@Override
	public void onConnectFailed(int reasonCode)
	{
		System.out.println("onConnectFailed");
	}
});
```

对于非Java SDK的用户，用户可以根据设备三元组信息自行组织MQTT CONNECT报文参数，进行设备登录：


mqtt的Connect报文参数如下：
```
  mqttClientId: clientId+"|securemode=2,signmethod=hmacsha1,timestamp=132323232|"
  mqttUsername: deviceKey+"&"+productKey
  mqttPassword: uppercase(sign_hmac(deviceSecret,content))
 ```
其中`clientId`可以用户自行定义，`timestamp`可以采用当前时间戳，
sign签名需要把以下参数按字典排序后，根据`signmethod`加签,并将签名结果转成大写。

* content的值为提交给服务器的参数（productKey、deviceKey、timestamp和clientId），按照字母顺序排序, 然后将参数值依次拼接。
* clientId：表示客户端ID，建议使用设备的MAC地址或SN码，64字符内。需要与mqttClientId中设置的clientId字段一致。
* timestamp：表示当前时间毫秒值，可以不传递。需要与mqttClientId中设置的timestamp字段一致。
* mqttClientId：格式中||内为扩展参数。
* signmethod：表示签名算法类型。当前版本下请使用hmacsha1
* securemode：表示目前安全模式，当前版本下请填写字段2

For example:

When clientId = 123, deviceKey = test, productKey = 123, timestamp = 1524448722000, deviceSecret=deviceSecret

sign= toUpperCase(hmacsha1(clientId123deviceKeytestproductKey123timestamp1524448722000deviceSecret))

在构建MqttClient的参数中，product， productKey，deviceKey以及deviceSecret可以从控制台中获取，或者通过EnOS REST API进行获取。

### 发送命令

#### 发送上行命令（从设备到EnOS Cloud）
当连接成功后，我们就可以发送命令了，比如，以下样例代码在回调函数中让子设备进行login操作。

这里解释下回调函数的意图，由于网关型设备如果断链后，服务端会自动把此网关型设备拓扑结构中的子设备全部自行下线。但是由于MQTT客户端允许自动重连，所以当识别到断线连接后，会主动触发onConnectLost回调。当自动重连生效后，应该把子设备的上线的逻辑放于onConnectSuccess回调方法中。

```
@Override
public void onConnectSuccess()
{
    try
    {
        System.out.println("start register login sub-device , current status : " + client.isConnected());
        SubDeviceLoginRequest request = SubDeviceLoginRequest.builder().setSubDeviceInfo(subProductKey, subDeviceKey, subDeviceSecret).build();
        SubDeviceLoginResponse rsp = client.publish(request);
        System.out.println(rsp);
    }
    catch (Exception e)
    {
        e.printStackTrace();
    }
}
```


*注意，子设备的productKey同样需要通过[控制台](http://tapd.oa.com)，或者[RestfulAPI](http://tapd.oa.com)进行获取。关于子设备的deviceKey, deviceSecret, 除了可以通过[控制台](http://tapd.oa.com)或[RestfulAPI](http://tapd.oa.com)外，我们也可以使用MQTT SDK的SubDeviceDynamicRegRequest来进行注册。*

接下来我们发送一个子设备的测点数据给到服务端

```
public static void postMeasurepoint()
{
      Map<String, String> struct = new HashMap<>();
      struct.put("structKey", "structValue");
      MeasurepointPostRequest request = MeasurepointPostRequest.builder()
                     .setProductKey(subProductKey).setDeviceKey(subDeviceKey)
                     .addMeasurePoint("p1", "string")
                     .addMeasreuPointWithQuality("p2", "value", 2)
                     .addMeasurePoint("p3", 100.2)
                     .addMeasurePoint("p4", struct)
                     .build();
      client.fastPublish(request);
}
```
> 这里看见，我们用了一个fastPublish方法了，此方法是不用关注返回值，做为测点，我们也不会提供返回值，client里面还有如下两个pulish的方法：

```
/**
 * publish the sync request and wait for the response
 * @throws Exception
 */
public <T extends IMqttResponse> T publish(IMqttRequest<T> request) throws Exception

/**
 * publish the request and register the callback , the callback will be called when rcv the response
 * @throws Exception
 */
public <T extends IMqttResponse> void publish(IMqttRequest<T> request, IResponseCallback<T> callback)
```

两者之间的区别在于：带有回调的publish方法是异步的，带有返回参数的publish方法是同步的，这里还需要注意，如果MeasurepointPostRequest不小心调用了同步的push，那么会一直等待，但是服务端除了错误之外，并没有返回值，所以会一直等到超时。

> 同时设备端提供了上报测点数据质量位的接口，用户通过该接口发送带有质量位的测点数据
#### 发送下行命令（从EnOS Cloud到设备）
下面我介绍下如何处理下行消息，下行消息主要是置数，设备服务调用等，在sdk中是以Command来表示的，设备端可以在onMessage回调函数中，设置响应消息，sdk会返回响应的响应消息给云端。
用户可以主动设置响应消息的错误码及错误信息，表示设备端响应该下行指令失败，云端会受到并返回该错误信息及错误码。对于用户自定义的错误码及错误信息。我们开放了2000及以上的错误码，作为设备端的错误码响应。

比如以下实例，我监听了测点置数的事件以及服务端禁用某个设备的事件。

```
 client.setArrivedMsgHandler(MeasurepointSetCommand.class, (MeasurepointSetCommand command, List<String> argList)->{
    boolean success = true;
    if(success){
        return MeasurepointSetReply.builder().build();
    }
    else {
        return MeasurepointSetReply.builder()
                .setCode(2000)
                .setMessage("handle the measurepoint set command failed")
                .build();
    }
});

client.setArrivedMsgHandler(SubDeviceDisableCommand.class, (SubDeviceDisableCommand command, List<String> argList)->{
    //if you donnt want to reply the message ,  just return null
    System.out.println(command);
    return null;
});
```

> 说明：
> 如果用户返回一个null的reply 那么设备端执行完成回调方法后，不作任何响应，不会返回reply消息给云端。
> 如果用户返回了一个有效的reply，那么sdk会根据这个reply进行序列化自动发送给服务端。
> 如果用户执行回调方法成功，用户设置返回码为200或者无需设置返回码；对于执行失败，用户需要设置2000及以上的用户自定义错误码及错误消息，返回给云端。

至此，sdk的大体功能就介绍完了，上下行消息可以再sdk中自行去寻找，用法都大同小异。

### 完整示例代码

```java
import com.envisioniot.enos.iot_mqtt_sdk.core.IConnectCallback;
import com.envisioniot.enos.iot_mqtt_sdk.core.MqttClient;
import com.envisioniot.enos.iot_mqtt_sdk.core.exception.EnvisionException;
import com.envisioniot.enos.iot_mqtt_sdk.core.msg.IMessageHandler;
import com.envisioniot.enos.iot_mqtt_sdk.message.downstream.device.SubDeviceDisableCommand;
import com.envisioniot.enos.iot_mqtt_sdk.message.downstream.tsl.MeasurepointSetCommand;
import com.envisioniot.enos.iot_mqtt_sdk.message.downstream.tsl.MeasurepointSetReply;
import com.envisioniot.enos.iot_mqtt_sdk.message.downstream.tsl.ServiceInvocationCommand;
import com.envisioniot.enos.iot_mqtt_sdk.message.downstream.tsl.ServiceInvocationReply;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.register.DeviceRegisterRequest;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.register.DeviceRegisterResponse;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.status.SubDeviceLoginRequest;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.status.SubDeviceLoginResponse;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.status.SubDeviceLogoutRequest;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.status.SubDeviceLogoutResponse;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.topo.*;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.tsl.EventPostRequest;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.tsl.MeasurepointPostRequest;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.tsl.TslTemplateGetRequest;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.tsl.TslTemplateGetResponse;
import com.envisioniot.enos.iot_mqtt_sdk.util.Pair;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author zhensheng.cai
 * @date 2018/8/3.
 */
public class SimpleSendReceive {
    private static final String local = "tcp://localhost:11883";
    private static final String alpha = "tcp://10.24.10.56:11883";
    private static final String prd = "tcp://10.24.8.76:11883";


    //alpha环境网关设备三元组
    private static final String productKey = "invu9zyT";
    public static final String deviceKey = "m7plCgtarp";
    public static final String deviceSecret = "t3O5bRTfTYJ9UMS2wCrb";

    //alpha环境子设备三元组
    public static final String subProductKey = "ybuO63Oe";
    public static final String subDeviceKey = "96Iy2aWmv7";
    public static final String subDeviceSecret = "HUxm8Vcm7sod0v6XV8I3";


    //alpha环境直连设备三元组
    private static final String productKey2 = "ybuO63Oe";
    public static final String deviceKey2 = "NnSM8B1Wrk";
    public static final String deviceSecret2 = "jQmd3jgukTt7fMXmNF8i";


    //prd环境设备三元组
//    private static final String productKey = "NyDmJcbZ";
//    public static final String deviceKey = "xCPLxZtLKg";
//    public static final String deviceSecret = "0sfmTw2c9gY5JcopMrvd";

    //prd环境子设备三元组
//    public static final String subProductKey = "muB7helV";
//    public static final String subDeviceKey = "UKaQFBAemf";
//    public static final String subDeviceSecret = "MEwVRDFptW6YctnS2GlF";


    private static MqttClient client;


    public static void init() {
        try {
            client = new MqttClient(alpha, productKey, deviceKey, deviceSecret);
            client.connect(new IConnectCallback() {
                @Override
                public void onConnectSuccess() {
                    try {
                        System.out.println("start register login sub-device , current status : " + client.isConnected());
                        SubDeviceLoginRequest request = SubDeviceLoginRequest.builder()
                                .setSubDeviceInfo(subProductKey, subDeviceKey, subDeviceSecret)
                                .build();
                        SubDeviceLoginResponse rsp = client.publish(request);
                        ;
                        System.out.println(rsp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConnectLost() {

                }

                @Override
                public void onConnectFailed(int reasonCode) {

                }
            });
        } catch (EnvisionException e) {
            e.printStackTrace();
        }
        System.out.println("connect:" + client.isConnected());
    }

    public static void initWithCallback() {
        System.out.println("start connect with callback ... ");
        try {
            client = new MqttClient(alpha, productKey, deviceKey, deviceSecret);
            client.getProfile().setConnectionTimeout(10);
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

    public static void initWithCallback(String env, String productKey, String deviceKey, String deviceSecret) {
        System.out.println("start connect with callback ... ");
        try {
            client = new MqttClient(env, productKey, deviceKey, deviceSecret);
            client.getProfile().setConnectionTimeout(10);
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

    public static void subDeviceRegister() {
        System.out.println("start register register sub-device , current status : " + client.isConnected());
        DeviceRegisterRequest request = DeviceRegisterRequest.builder()
                .addSubRegisterInfo("ybuO63Oe", "NB101", "NB101", "NB101")
                .addSubRegisterInfo("ybuO63Oe", "NB102", "NB102", "NB102")
                .addSubRegisterInfo("ybuO63Oe", "NB103", "NB103", "NB103")
                .build();
        DeviceRegisterResponse rsp = null;
        try {

            rsp = client.publish(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("--->" + rsp);
    }


    public static void subDeviceLogin() {
        System.out.println("start register login sub-device , current status : " + client.isConnected());
        SubDeviceLoginRequest request = SubDeviceLoginRequest.builder()
                .setSubDeviceInfo(subProductKey, subDeviceKey, subDeviceSecret).build();
        SubDeviceLoginResponse rsp = null;

        try {
            rsp = client.publish(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(rsp);
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
    }


    public static void addTopo() throws Exception {
        System.out.println("start add topo ...");
        TopoAddRequest request = TopoAddRequest.builder().
                addSubDevice(new SubDeviceInfo(subProductKey, subDeviceKey, subDeviceSecret)).build();
        TopoAddResponse rsp = client.publish(request);
        System.out.println("-->" + rsp);
        getTopo();

    }

    public static void deleteTopo() throws Exception {
        System.out.println("start delete topo...");
        TopoDeleteRequest request = TopoDeleteRequest.builder()
                .setSubDevices(Lists.newArrayList(Pair.makePair(subProductKey, subDeviceKey))).build();
        TopoDeleteResponse rsp = client.publish(request);
        System.out.println("-->" + rsp);
        getTopo();
    }


    public static void getTopo() throws Exception {
        System.out.println("start get topo...");
        TopoGetRequest request = TopoGetRequest.builder().build();

        TopoGetResponse rsp = client.publish(request);
        System.out.println("-->" + rsp);

    }

    public static void getTslTemplete() {
        System.out.println("start get tsl template... ");
        TslTemplateGetRequest request = TslTemplateGetRequest.builder().setProductKey(productKey).setDeviceKey(deviceKey).build();
        TslTemplateGetResponse rsp = null;
        try {
            rsp = client.publish(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(rsp);
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
        System.out.println("start post measurepoint ...");
        MeasurepointPostRequest.Builder builder = MeasurepointPostRequest.builder().setProductKey(subProductKey).setDeviceKey(subDeviceKey);
        builder.addMeasurePoint("INV.PVPowIn", "11.50");
        builder.addMeasurePoint("INV.GenActivePW", "100.20");
        MeasurepointPostRequest request = builder.build();
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


    public static void handleDisableDevice() {
        client.setArrivedMsgHandler(SubDeviceDisableCommand.class, (arrivedMessage, argList) -> {
            System.out.println(argList);
            return null;
        });
    }


    public static void handleServiceInvocation() {
        IMessageHandler<ServiceInvocationCommand, ServiceInvocationReply> handler = new IMessageHandler<ServiceInvocationCommand, ServiceInvocationReply>() {
            @Override
            public ServiceInvocationReply onMessage(ServiceInvocationCommand request, List<String> argList) throws Exception {
                System.out.println("rcvn async serevice invocation command" + request + " topic " + argList);
                return ServiceInvocationReply.builder().addOutputData("pointA", "valueA")
                        .build();
            }
        };


        client.setArrivedMsgHandler(ServiceInvocationCommand.class, handler);
    }


    public static void measurepointSetHandler() {
        client.setArrivedMsgHandler(MeasurepointSetCommand.class, new IMessageHandler<MeasurepointSetCommand, MeasurepointSetReply>() {
            @Override
            public MeasurepointSetReply onMessage(MeasurepointSetCommand arrivedMessage, List<String> argList) throws Exception {
                boolean success = true;
                if (success) {
                    return MeasurepointSetReply.builder().build();
                } else {
                    return MeasurepointSetReply.builder()
                            .setCode(2000)
                            .setMessage("handle the measurepoint set command failed")
                            .build();
                }
            }
        });

        client.setArrivedMsgHandler(SubDeviceDisableCommand.class, (arrivedMessage, argList) -> {
            System.out.println(argList);
            return null;
        });
    }


    public static void main(String[] args) throws Exception {

//demo1：网关+子设备，设备拓扑操作，发送测点数据，设备上线、下线
//        initWithCallback();
//        addTopo();
//        subDeviceLogin();
//        postMeasurepoint();
//        getTslTemplete();
//        getTslTemplete(subProductKey, subDeviceKey);
//        subdeviceLogout();

//demo2：测试直连设备
//        initWithCallback(alpha, productKey2, deviceKey2, deviceSecret2);
//        postMeasurepoint(productKey2, deviceKey2);
//        getTslTemplete(productKey2, deviceKey2);

//demo3：网关+子设备的动态注册
//        initWithCallback();
//        subDeviceRegister("WbfKpbjl", "NB101", "NB101", "NB101");

//demo4：下行发送消息-设备禁用
//        initWithCallback();
//        handleDisableDevice();
    }


}

```



![packages](https://github.com/EnvisionIot/enos-iot-mqtt-java-sdk/blob/master/src/main/resources/imgs/tapd_20716331_base64_1534760042_26.png)

