package com.envisioniot.enos.iot_mqtt_sdk.message.downstream.tsl;

import com.envisioniot.enos.iot_mqtt_sdk.core.exception.EnvisionException;
import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.DeliveryTopicFormat;
import com.envisioniot.enos.iot_mqtt_sdk.message.downstream.BaseMqttReply;
import com.envisioniot.enos.iot_mqtt_sdk.util.CheckUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhensheng.cai
 * @date 2018/7/12.
 */
public class ServiceInvocationReply extends BaseMqttReply {
    private static final long serialVersionUID = 1506910581305477787L;
    private String serviceIdentifier;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends BaseMqttReply.Builder<Builder, ServiceInvocationReply> {
        private Map<String, Object> map = new HashMap<>();

        public Builder addOutputData(String point, Object value) {
            map.put(point, value);
            return this;
        }

        public Builder addOutputDatas(Map<String, Object> values) {
            map.putAll(values);
            return this;
        }

        public Builder setOutputDatas(Map<String, Object> values) {
            map = values;
            return this;
        }


        @Override
        protected Object createData() {
            return map;
        }

        @Override
        protected ServiceInvocationReply createRequestInstance() {
            return new ServiceInvocationReply();
        }
    }

    @Override
    public void setTopicArgs(List<String> args) {
        if (args.size() == 3) {
            this.setProductKey(args.get(0));
            this.setDeviceKey(args.get(1));
            this.setServiceIdentifier(args.get(2));
        } else {
            throw new UnsupportedOperationException("topic args size not match!");
        }
    }


    @Override
    public void check() throws EnvisionException {
        super.check();
        CheckUtil.checkNotEmpty(serviceIdentifier, "service.identifier");
    }

    @Override
    public String getMessageTopic() {
        return String.format(_getPK_DK_FormatTopic(), getProductKey(), getDeviceKey(), serviceIdentifier);
    }

    public String getServiceIdentifier() {
        return serviceIdentifier;
    }

    public void setServiceIdentifier(String serviceIdentifier) {
        this.serviceIdentifier = serviceIdentifier;
    }

    @Override
    protected String _getPK_DK_FormatTopic() {
        return DeliveryTopicFormat.SERVICE_INVOKE_REPLY;
    }
}
