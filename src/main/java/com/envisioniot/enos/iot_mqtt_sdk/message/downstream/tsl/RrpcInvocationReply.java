package com.envisioniot.enos.iot_mqtt_sdk.message.downstream.tsl;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.DeliveryTopicFormat;
import com.envisioniot.enos.iot_mqtt_sdk.message.downstream.BaseMqttReply;

import java.util.List;

/**
 * 对于RRPC的reply 其实是一个透传的reply 只需要保证云端能够收到返回就可以了，甚至可以是二进制的返回
 * <p>
 * 对于TSL服务的同步调用，在云端会转化成rrpc调用，会在云端先进行tsl的参数的检查，只有符合参数条件的请求会下发到云端，
 * 云端下发的参数，即为设备端收到的参数
 * ：{"method":"thing.service.syncservicewithparam","id":"23624456","params":{"power":100},"version":"1.0.0"},
 * topic=/sys/a1TB8qpr6bT/collect_gateway/rrpc/request/1018733446471622656
 *
 * @author zhensheng.cai
 * @date 2018/7/13.
 */
public class RrpcInvocationReply extends BaseMqttReply {
    private static final long serialVersionUID = -8875307064989466897L;
    private String rrpcMessageId;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends BaseMqttReply.Builder<Builder, RrpcInvocationReply> {
        private Object data;

        public Builder setData(Object data) {
            this.data = data;
            return this;
        }

        @Override
        protected Object createData() {
            return data;
        }

        @Override
        protected RrpcInvocationReply createRequestInstance() {
            return new RrpcInvocationReply();
        }
    }


    @Override
    public void setTopicArgs(List<String> args) {
        if (args.size() == 3) {
            this.setProductKey(args.get(0));
            this.setDeviceKey(args.get(1));
            this.setRrpcMessageId(args.get(2));
        } else {
            throw new UnsupportedOperationException("topic args size not match!");
        }

    }

    @Override
    public String getMessageTopic() {
        return String.format(_getPK_DK_FormatTopic(), getProductKey(), getDeviceKey(), this.rrpcMessageId);
    }


    @Override
    protected String _getPK_DK_FormatTopic() {
        return DeliveryTopicFormat.RRPC_REPLY;
    }

    public String getRrpcMessageId() {
        return rrpcMessageId;
    }

    public RrpcInvocationReply setRrpcMessageId(String rrpcMessageId) {
        this.rrpcMessageId = rrpcMessageId;
        return this;
    }
}
