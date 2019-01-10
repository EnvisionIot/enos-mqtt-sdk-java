# Using EnOS Device SDK for MQTT for Java

This article instructs how to prepare your development environment to use the *EnOS Device SDK for MQTT for Java*.

- [Installing Java JDK SE](https://github.com/EnvisionIot/enos-mqtt-sdk-java#installjava)
- [Installing Maven](https://github.com/EnvisionIot/enos-mqtt-sdk-java#installmaven)
- Obtaining EnOS Device SDK for MQTT for Java
  - [Including Dependency in Maven Project](https://github.com/EnvisionIot/enos-mqtt-sdk-java#installiotmaven)
  - [Building from Source Code](https://github.com/EnvisionIot/enos-mqtt-sdk-java#installiotsource)
- [Feature List](https://github.com/EnvisionIot/enos-mqtt-sdk-java#featurelist)
- [Sample Code](https://github.com/EnvisionIot/enos-mqtt-sdk-java#samplecode)

## Installing Java JDK SE

To use the EnOS Device SDK for MQTT for Java, you will need to install **Java SE 8**.

## Installing Maven

To use EnOS Device SDK for MQTT for Java, we recommend you use **Maven 3**.

## Obtaining EnOS Device SDK for MQTT for Java

You can obtain the EnOS Device SDK for MQTT for Java through the following methods:

- Include the project as a dependency in your Maven project
- Download the source code by cloning this repo and build on your machine

### Including Dependency in Maven Project

*This is the recommended method of including the EnOS IoT SDKs in your project.*

- Navigate to [http://search.maven.org](http://search.maven.org/), search for **com.envisioniot.enos** and take note of the latest version number (or the version number of whichever version of the sdk you desire to use).

- In your main pom.xml file, add the EnOS Device SDK for MQTT for Java as a dependency as follows:

  ```
  <dependency>
      <groupId>com.envisioniot</groupId>
      <artifactId>enos-mqtt</artifactId>
      <version>2.1.0</version>
      <!--You might need to change the version number as you need.-->
  </dependency>
  ```

### Building from Source Code

- Get a copy of the **EnOS Device SDK for MQTT for Java** from master branch of the GitHub (current repo). You should fetch a copy of the source from the **master** branch of the GitHub repository: <https://github.com/EnvisionIot/enos-iot-mqtt-java-sdk>

  ```
  git clone https://github.com/EnvisionIot/enos-iot-mqtt-java-sdk.git
  ```

- When you have obtained a copy of the source, you can build the SDK for Java.

## Key Features

The EnOS Device SDK for MQTT for Java supports the following functions:

- Registration of devices
- Add, update, query, or delete of gateway devices
- Online and offline of sub-devices
- Create or delete of device tags
- Create device measurepoints
- Upload device alerts 
- Upload device messages
- Set device measurepoints and get measurepoint data
- Enable device services
- Send messages on device startup, stop, and delete

## Sample Code

The following sample codes instruct how to use the EnOS Device SDK for MQTT for Java.

### Connecting to Server

```
MqttClient client = new MqttClient(serverUrl, productKey, deviceKey, deviceSecret);
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

In the above sample, `serverUrl` is the address of the server. If using TCP connection, the format of the server URL can be `tcp://{regionUrl}:11883`. The `productKey`, `deviceKey`, and `deviceSecret` are the 3 major elements of the device information.

If you are not using Java SDK, you can create MQTT CONNECT parameters with the 3 major elements of device information for other programming languages. See the following example: 

```
  mqttClientId: clientId+"|securemode=2,signmethod=hmacsha1,timestamp=132323232|"
  mqttUsername: deviceKey+"&"+productKey
  mqttPassword: uppercase(sign_hmac(deviceSecret,content))
```

In which:

- The `clientId` parameter is the client ID, which must be the same with the `clientId` in `mqttClientId`. It is recommended to use the MAC or SN of the device.
- The `timestamp` parameter can be the current time, but it must be the same with the `timestamp` in `mqttClientId`. 
- The signature should be created using the `signmethod`.

- The value of the `content` parameter is the collection of parameters (productKe, deviceKey, timestamp, and clientId) sent to the server.  The parameters and their values should be listed concatenated by ASCII order.
- `signmethod`: The signature generating algorithm. Supported algorithm is `hmacsha1`.
- `securemode`：The current security mode. Supported value is `2`.

For example:

```
clientId = 123, deviceKey = test, productKey = 123, timestamp = 1524448722000, deviceSecret = deviceSecret

sign= toUpperCase(hmacsha1(clientId123deviceKeytestproductKey123timestamp1524448722000deviceSecret))
```

In the above sample, the product, productKey, deviceKey, and deviceSecret can be retrieved from EnOS platform or through EnOS REST API.

#### Connecting to Cloud through SSL/TLS

To ensure device security, users can enable the certificate-based bi-directional authentication method through the SSL/TLS protocol. Users can apply device certificate by calling the EnOS certificate service API, load the certificate to the SDK directory, and then connect to the server through the SSL port. The server URL format is `ssl://{regionUrl}:18883`. See the code sample below.

```
client = new MqttClient(betaSSL, productKey, deviceKey, deviceSecret);
client.getProfile().setSSLSecured(true).setSSLJksPath("path.jks", "password");
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
```

> Users can also use the `setSSLContext()` method to directly set the SSL context, load the certificate content, and complete initializing the MQTT client with certificate-base bi-directional authentication. 

### Sending Commands

#### From Device to EnOS Cloud

When connection is successful, commands can be sent from device to EnOS Cloud. For example, the following code is for login operation of a sub-device in a callback function.  

If network connection is broken for gateway devices, the service will regard all the sub-devices in the topology of the gateway device as offline. While the MQTT client allows automatic re-connection, when the network connection recovers, the `onConnectLost` callback function will be invoked. When the re-connection takes effect, the online logic of the sub-devices will be included in the `onConnectSuccess` callback method.

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

*Note that the productKey of the sub-device should be retrieved from the EnOS Console or through EnOS REST API. The deviceKey and deviceSecret can also be registered through the DeviceRegisterRequest of the SDK.*

Now, use the following code sample to send the measurepoint data of a sub-device to the cloud.

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

> In this sample, the fastPublish method is used. In this way, no response is provided for measurepoints. There are another 2 publish methods in the client.

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

The difference is: the publish method with callback is asynchronous, but the publish method with response parameters is synchronous. Note that if MeasurepointPostRequest calls a push command wongly,  the service does not respond any error messages until session timeout.

> The device side provides an API for uploading measurepoint data with quality. Users can call this API to send measurepoint data with quality.

#### From EnOS Cloud to Device

The following section introduces how to handle the downstream commands sent from EnOS cloud to device. On the device side, response messages can be configured in the `onMessage` callback function, so that the SDK can respond the messages to the cloud. Users can configure the error codes and error messages to indicate failure of commands to the device. Users can choose to use 2000 and above numbers for this kind of customized error codes and error messages.

In the following code sample, the setting measurepoint and disabled device events are monitored.

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

> Note: If the user responds a null reply, the device side does not respond after executing the callback method. If the user responds an effective reply, the SDK will serialize the reply and send it to the cloud automatically. If the callback method is executed successfully, the user can set a response code 200 or not. For failed execution, the user can set a customized error code 2000 or above to be sent to the cloud. 

### End-to-End Sample Code

You can find the sample code from `<dir>/blob/master/src/main/java/com/envisioniot/enos/iot_mqtt_sdk/sample/SimpleSendReceive.java` too, where, `<dir>` is the directory of this SDK in your local store.

```
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


    // Device information in alpha environment
    private static final String productKey = "invu9zyT";
    public static final String deviceKey = "m7plCgtarp";
    public static final String deviceSecret = "t3O5bRTfTYJ9UMS2wCrb";

    // Sub-device information in alpha environment
    public static final String subProductKey = "ybuO63Oe";
    public static final String subDeviceKey = "96Iy2aWmv7";
    public static final String subDeviceSecret = "HUxm8Vcm7sod0v6XV8I3";


    // Direct-connection device information in alpha environment
    private static final String productKey2 = "ybuO63Oe";
    public static final String deviceKey2 = "NnSM8B1Wrk";
    public static final String deviceSecret2 = "jQmd3jgukTt7fMXmNF8i";


    // Device information in prd environment
//    private static final String productKey = "NyDmJcbZ";
//    public static final String deviceKey = "xCPLxZtLKg";
//    public static final String deviceSecret = "0sfmTw2c9gY5JcopMrvd";

    // Sub-device information in prd environment
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
             * In the current edition, Post message does not have reply.
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
             * In the current edition, Post message does not have reply.
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

// demo1: Gateway+subDevice, operations on device topology, sending measurepoint data, setting 
// device online/offline
//        initWithCallback();
//        addTopo();
//        subDeviceLogin();
//        postMeasurepoint();
//        getTslTemplete();
//        getTslTemplete(subProductKey, subDeviceKey);
//        subdeviceLogout();

//demo2: Testing device direct connection
//        initWithCallback(alpha, productKey2, deviceKey2, deviceSecret2);
//        postMeasurepoint(productKey2, deviceKey2);
//        getTslTemplete(productKey2, deviceKey2);

//demo3: Dynamic registration of gateway+subDevice
//        initWithCallback();
//        subDeviceRegister("WbfKpbjl", "NB101", "NB101", "NB101");

//demo4：Sending command from cloud to device - disable device
//        initWithCallback();
//        handleDisableDevice();
    }


}
```

The following screen capture shows the directory of commands in the EnOS Device SDK for MQTT.

[![packages](https://github.com/EnvisionIot/enos-iot-mqtt-java-sdk/raw/master/src/main/resources/imgs/tapd_20716331_base64_1534760042_26.png)](https://github.com/EnvisionIot/enos-iot-mqtt-java-sdk/blob/master/src/main/resources/imgs/tapd_20716331_base64_1534760042_26.png)
