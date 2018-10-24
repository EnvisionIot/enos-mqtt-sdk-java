package com.envisioniot.enos.iot_mqtt_sdk.message.upstream;

import com.envisioniot.enos.iot_mqtt_sdk.core.msg.IMqttResponse;
import com.envisioniot.enos.iot_mqtt_sdk.message.BaseAckMessage;

/**
 * @author zhensheng.cai
 * @date 2018/7/6.
 */
public abstract class BaseMqttResponse extends BaseAckMessage implements IMqttResponse {
    private static final long serialVersionUID = 737188314780453682L;
    private String productKey;
    private String deviceKey;
    private String messageTopic;


    @Override
    public String getMessageId() {
        return getId();
    }

    @Override
    public String getMessageTopic() {
        return messageTopic;
    }

    @Override
    public void setMessageTopic(String topic) {
        this.messageTopic = topic;
    }

    @Override
    public void setMessageId(String msgId) {
        setId(msgId);
    }

    @Override
    public String getProductKey() {
        return this.productKey;
    }

    @Override
    public String getDeviceKey() {
        return this.deviceKey;
    }

    @Override
    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    @Override
    public void setDeviceKey(String deviceKey) {
        this.deviceKey = deviceKey;
    }
}
