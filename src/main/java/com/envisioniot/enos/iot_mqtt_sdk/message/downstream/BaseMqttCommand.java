package com.envisioniot.enos.iot_mqtt_sdk.message.downstream;

import com.envisioniot.enos.iot_mqtt_sdk.core.msg.IMqttCommand;
import com.envisioniot.enos.iot_mqtt_sdk.message.BaseAnswerableMessage;

/**
 * @author zhensheng.cai
 * @date 2018/7/10.
 */
public abstract class BaseMqttCommand<T> extends BaseAnswerableMessage<T> implements IMqttCommand<T> {

    private static final long serialVersionUID = -3588543384705787861L;
    private String productKey;
    private String deviceKey;
    private String messageTopic;


    @Override
    public String getAnswerTopic() {
        return getMessageTopic() + "_reply";
    }


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
        return productKey;
    }

    @Override
    public String getDeviceKey() {
        return deviceKey;
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
