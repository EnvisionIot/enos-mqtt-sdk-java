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
      MeasurepointPostRequest request = MeasurepointPostRequest.builder()
      .setProductKey(subProductKey).setDeviceKey(subDeviceKey)
      .addMeasurePoint("p1", "string")
      .addMeasurePoint("p2", "{'value':123.4,  'quality':2}")
      .addMeasurePoint("p3", 100.2)
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

#### 发送下行命令（从EnOS Cloud到设备）
下面我介绍下如何处理下行消息，下行消息主要是置数，设备服务调用等，在sdk中是以Command来表示的，比如以下实例，我监听了测点置数的事件以及服务端禁用某个设备的事件。


```
client.setArrivedMsgHandler(MeasurepointSetCommand.class, new IMessageHandler<MeasurepointSetCommand, MeasurepointSetReply>()
{
    @Override
    public MeasurepointSetReply onMessage(MeasurepointSetCommand arrivedMessage, List<String> argList) throws Exception
    {
        return null;
    }
});

client.setArrivedMsgHandler(DisableDeviceCommand.class, new IMessageHandler<DisableDeviceCommand, DisableDeviceCommandReply>()
{

    @Override
    public DisableDeviceCommandReply onMessage(DisableDeviceCommand command, List<String> argList) throws Exception
    {
        // disable device and ret 200 reply
        return DisableDeviceCommandReply.builder()
        .setCode(200).build();
    }
});
```

如果用户返回一个null的reply，那么sdk会认为客户端不支持这个动作，会自行构造通用的reply的告知服务端。

如果用户返回了一个有效的reply，那么sdk会根据这个reply进行序列化自动发送给服务端。

至此，sdk的大体功能就介绍完了，上下行消息可以再sdk中自行去寻找，用法都大同小异。

![packages](https://github.com/EnvisionIot/enos-iot-mqtt-java-sdk/blob/master/src/main/resources/imgs/tapd_20716331_base64_1534760042_26.png)

