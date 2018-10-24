package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.tsl;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import com.envisioniot.enos.iot_mqtt_sdk.core.IConnectCallback;
import com.envisioniot.enos.iot_mqtt_sdk.core.MqttClient;
import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.ArrivedTopicPattern;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.BaseMqttResponse;
import com.envisioniot.enos.iot_mqtt_sdk.sample.SimpleSendReceive;

/**
 * @author zhensheng.cai
 * @date 2018/7/10.
 */
public class ModelUpRawResponse extends BaseMqttResponse {
    private static final long serialVersionUID = -3130733082784463467L;

    private static Pattern pattern = Pattern.compile(ArrivedTopicPattern.MODEL_UP_RAW_REPLY);

    @Override
    public Pattern getMatchTopicPattern() {
        return pattern;
    }

    private byte[] payload;

    public ModelUpRawResponse()
    {}

    public ModelUpRawResponse(byte[] payload)
    {
        this.payload = payload;
    }

    public void setPayload(byte[] payload)
    {
        this.payload = payload;
    }

    public byte[] getPayload()
    {
        return payload;
    }

    @Override
    public DecodeResult decode(String topic, byte[] payload) {
        List<String> path = this.match(topic);
        if (path == null) {
            return null;
        }

        ModelUpRawResponse arrivedMsg = new ModelUpRawResponse();
        arrivedMsg.setPayload(payload);
        arrivedMsg.setId("unknown");
        arrivedMsg.setMessageTopic(topic);
        if (path.size() > 0) {
            arrivedMsg.setProductKey(path.get(0));
        }
        if (path.size() > 1) {
            arrivedMsg.setDeviceKey(path.get(1));
        }
        return new DecodeResult(arrivedMsg, path);
    }

    @Override
    public String toString() {
        return "ModelUpRawResponse{" +
                "payload=" + Arrays.toString(payload) +
                "} ";
    }

    public static void main(String[] args) throws Exception {
        String rawProduct = "WwVF5nKj";
        String rawDevice = "device1";
        String rawSecret = "mOpcSKsxSQsvr0nUHUiK";

        MqttClient client = new MqttClient(SimpleSendReceive.local, rawProduct, rawDevice, rawSecret);
        client.connect(new IConnectCallback() {
            @Override
            public void onConnectSuccess() {

            }

            @Override
            public void onConnectLost() {

            }

            @Override
            public void onConnectFailed(int reasonCode) {

            }
        });
        System.out.println("connect succ ,start post raw");

        Random random = new Random();

        byte[] array = new byte[100];
        int index = 0;
        byte method = 0x01;
        array[index++] = method; // method;
        int id = 2;
        System.out.println("id: " + id);
        array[index++] = (byte) ((id >> 24) & 0xff);
        array[index++] = (byte) ((id >> 16) & 0xff);
        array[index++] = (byte) ((id >> 8) & 0xff);
        array[index++] = (byte) (id & 0xff);
        int prop_int16 = random.nextInt(10000);
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

        ModelUpRawResponse rsp = client.publish(ModelUpRawRequest.builder().setPayload(payload).build());
        System.out.println(rsp);

    }



}
