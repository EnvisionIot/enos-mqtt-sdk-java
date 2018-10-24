# Java MQTT SDK使用说明

----

#### SDK获取

​​我们按照此[MQTT协议规范](https://dev.envisioncn.com/devportal/index.html#/166/57baab5ed3eb4806104b045d/doccenter/DeviceConnection2.0/ZH/4@%E6%93%8D%E4%BD%9C%E6%8C%87%E5%8D%97/11@%E8%AE%BE%E5%A4%87%E7%AB%AF%E5%BC%80%E5%8F%91%E6%8C%87%E5%8D%97/2@SDK%E8%AE%BE%E5%A4%87%E7%AB%AF%E5%8D%8F%E8%AE%AE.md)，实现了一个Java版本，便于开发者专注于业务开发，无需关注协议细节，可以通过[GitHub](http://https://github.com/EnvisionIot/enos-iot-mqtt-java-sdk)获取源码。
<br/>
另外，我们也直接把jar包上传到了maven仓库，开发者只需引入此依赖即可。

```
<dependency>
  <groupId>com.envisioniot.enos</groupId>
  <artifactId>iot_mqtt_sdk</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```
<br/>

#### 功能说明

目前此预览版只是把基础的功能封装了，功能表如下：

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

<br/>

#### SDK使用

使用SDK非常简单，只要了解到上述能力后，可以很容易的和服务端进行对接，这里以一个简单的样例来告诉大家如何使用sdk。
<br/>

首先连接上服务器

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
> 对于非Java SDK的用户，用户可以根据设备三元组信息自行组织MQTT CONNECT报文参数，进行设备登录：

 ```
  mqtt的Connect报文参数：
  mqttClientId: clientId+"|securemode=2,signmethod=hmacsha1,timestamp=132323232|"
  mqttUsername: deviceKey+"&"+productKey
  mqttPassword: sign_hmac(deviceSecret,content)
 ```
其中clientId可以用户自行定义，timestamp可以采用当前时间戳，
sign签名需要把以下参数按字典排序后，根据signmethod加签。

* content的值为提交给服务器的参数（productKey、deviceKey、timestamp和clientId），按照字母顺序排序, 然后将参数值依次拼接。
* clientId：表示客户端ID，建议使用设备的MAC地址或SN码，64字符内。需要与mqttClientId中设置的clientId字段一致。
* timestamp：表示当前时间毫秒值，可以不传递。需要与mqttClientId中设置的timestamp字段一致。
* mqttClientId：格式中||内为扩展参数。
* signmethod：表示签名算法类型。当前版本下请使用hmacsha1
* securemode：表示目前安全模式，当前版本下请填写字段2

例如 clientId = 123，deviceKey = test， productKey = 123， timestamp = 1524448722000，deviceSecret=deviceSecret
sign= hmacsha1(deviceSecret, clientId123deviceKeytestproductKey123timestamp1524448722000)

> 在构建MqttClient的参数中，product， productKey，deviceKey以及deviceSecret应该从控制台中获取，或者通过[RestfulAPI](http://tapd.oa.com)进行获取。

这里解释下回调函数的意图，由于网关型设备如果断链后，服务端会自动把此网关型设备拓扑结构中的子设备全部自行下线。但是由于MQTT客户端允许自动重连，所以当识别到断线连接后，会主动触发onConnectLost回调。当自动重连生效后，应该把子设备的上线的逻辑放于onConnectSuccess回调方法中。

<br/>
当连接成功后，我们就可以发送命令了，比如，我这里在回调函数中让子设备进行login操作。

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


> 注意，子设备的productKey同样需要通过[控制台](http://tapd.oa.com)，或者[RestfulAPI](http://tapd.oa.com)进行获取。关于子设备的deviceKey, deviceSecret, 除了可以通过[控制台](http://tapd.oa.com)或[RestfulAPI](http://tapd.oa.com)外，我们也可以使用MQTT SDK的SubDeviceDynamicRegRequest来进行注册。

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

> 两者之间的区别在于：带有回调的publish方法是异步的，带有返回参数的publish方法是同步的，这里还需要注意，如果MeasurepointPostRequest不小心调用了同步的push，那么会一直等待，但是服务端除了错误之外，并没有返回值，所以会一直等到超时。

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
<br/>
如果用户返回了一个有效的reply，那么sdk会根据这个reply进行序列化自动发送给服务端。

至此，sdk的大体功能就介绍完了，上下行消息可以再sdk中自行去寻找，用法都大同小异。

<br/>

![packages](https://github.com/EnvisionIot/enos-iot-mqtt-java-sdk/blob/master/src/main/resources/imgs/tapd_20716331_base64_1534760042_26.png)


----------
这个预览版本接口还是比较粗糙的，后续版本，我们会根据序列化反序列化，request， response， command，reply对象进行优化。







