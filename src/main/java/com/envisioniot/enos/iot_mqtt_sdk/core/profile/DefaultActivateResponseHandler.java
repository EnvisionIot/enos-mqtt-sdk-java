package com.envisioniot.enos.iot_mqtt_sdk.core.profile;

import com.envisioniot.enos.iot_mqtt_sdk.core.MqttClient;
import com.envisioniot.enos.iot_mqtt_sdk.core.msg.IMessageHandler;
import com.envisioniot.enos.iot_mqtt_sdk.message.downstream.activate.DeviceActivateInfoCommand;
import com.envisioniot.enos.iot_mqtt_sdk.message.downstream.activate.DeviceActivateInfoReply;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.ResponseCode;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.register.DeviceBasicInfo;
import com.envisioniot.enos.iot_mqtt_sdk.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.FieldConstants.*;

/**
 * default handler of {@link DeviceActivateInfoCommand} which handle the response with {@link FileProfile} and {@link com.envisioniot.enos.iot_mqtt_sdk.core.internals.MqttConnection}
 * it persistent the reply deviceSecret and recreate the connection
 * @author zhensheng.cai
 * @date 2019/1/3.
 */
public class DefaultActivateResponseHandler implements IMessageHandler<DeviceActivateInfoCommand,DeviceActivateInfoReply> {

    private static Logger logger = LoggerFactory.getLogger(DefaultActivateResponseHandler.class);
    public FileProfile profile;
    public MqttClient client;


    public DefaultActivateResponseHandler(FileProfile profile, MqttClient client) {
        this.profile = profile;
        this.client = client;

    }

    @Override
    public DeviceActivateInfoReply onMessage(DeviceActivateInfoCommand response, List<String> argList) throws Exception {
        logger.info("handle activate reply info {}  by default handler", response.toString());
        DeviceBasicInfo deviceInfo = response.getDeviceInfo();
        String deviceSecret = deviceInfo.deviceSecret;
        if (StringUtil.isNotEmpty(deviceSecret) && StringUtil.isEmpty(this.profile.getProperties().getProperty(DEVICE_SECRET))) {
            this.profile.getProperties().setProperty(DEVICE_SECRET, deviceSecret);
            if (StringUtil.isNotEmpty(deviceInfo.assetId)) {
                this.profile.getProperties().setProperty(ASSET_ID, deviceInfo.assetId);
            }
            this.profile.persistent();
        }
        //publish the reply by handler and then recreate the mqtt connection
        DeviceActivateInfoReply reply = DeviceActivateInfoReply.builer()
                .setCode(ResponseCode.SUCCESS)
                .setMessage("persistent the device secret success")
                .setProductKey(response.getProductKey())
                .setDeviceKey(response.getDeviceKey())
                .build();
        reply.setMessageId(response.getMessageId());
        this.client.fastPublish(reply);
        this.client.rebuildConnection();
        return null;
    }


}
