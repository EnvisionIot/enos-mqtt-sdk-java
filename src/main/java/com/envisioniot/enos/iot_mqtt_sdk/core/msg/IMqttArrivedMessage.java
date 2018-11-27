package com.envisioniot.enos.iot_mqtt_sdk.core.msg;

import com.envisioniot.enos.iot_mqtt_sdk.util.GsonUtil;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Notice: 对于MqttArrivedMessage需要有一个默认的构造函数。 arrived mqtt message includes
 * downstream command {@link IMqttCommand} response of upstream
 * request{@link IMqttResponse}
 *
 * @author zhensheng.cai
 * @date 2018/7/6.
 */
public interface IMqttArrivedMessage extends IMqttMessage {
    Pattern getMatchTopicPattern();

    default List<String> match(String topic) {
        Matcher matcher = this.getMatchTopicPattern().matcher(topic);
        if (matcher.matches()) {
            String[] groups = new String[matcher.groupCount()];
            for (int i = 0; i < matcher.groupCount(); i++) {
                groups[i] = matcher.group(i + 1);
            }

            return Arrays.asList(groups);
        }
        return null;
    }


    /**
     * @param topic mqtt topic of arrived message
     * @param payload byte array of message payload
     * @return arrivedMessage & path
     * @throws Exception
     */
    public default DecodeResult decode(String topic, byte[] payload) throws Exception {

        List<String> path = this.match(topic);
        if (path == null) {
            return null;
        }

        IMqttArrivedMessage arrivedMsg = null;
        try {
            String msg = new String(payload, "UTF-8");
            arrivedMsg = GsonUtil.fromJson(msg, this.getClass());
        } catch (Exception e) {
            throw new RuntimeException("decode payload err:" + Arrays.toString(payload), e);
        }

        if (arrivedMsg == null) {
            throw new RuntimeException("can't decode payload:" + Arrays.toString(payload));
        }

        arrivedMsg.setMessageTopic(topic);

        if (path.size() > 0) {
            arrivedMsg.setProductKey(path.get(0));
        }

        if (path.size() > 1) {
            arrivedMsg.setDeviceKey(path.get(1));
        }
        return new DecodeResult(arrivedMsg, path);
    }

    public class DecodeResult {
        private IMqttArrivedMessage arrivedMsg;
        private List<String> pathList;

        public DecodeResult(IMqttArrivedMessage arrivedMsg, List<String> pathValueList) {
            super();
            this.arrivedMsg = arrivedMsg;
            this.pathList = pathValueList;
        }

        public List<String> getPathList() {
            return pathList;
        }

        public String getTopicPath(int index) {
            return pathList.get(index);
        }

        @SuppressWarnings("unchecked")
        public <T extends IMqttArrivedMessage> T getArrivedMsg() {
            return (T) arrivedMsg;
        }
    }
}
