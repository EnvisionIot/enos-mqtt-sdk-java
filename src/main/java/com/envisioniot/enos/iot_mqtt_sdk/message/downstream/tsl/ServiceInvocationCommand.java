package com.envisioniot.enos.iot_mqtt_sdk.message.downstream.tsl;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.ArrivedTopicPattern;
import com.envisioniot.enos.iot_mqtt_sdk.message.downstream.BaseMqttCommand;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 设备服务调用： 特指是设备服务异步调用， 同步的服务调用，专门用RRPC进行处理。
 * <p>
 * 接收到消息,来至Topic [/sys/a1TB8qpr6bT/collect_gateway/thing/service/asyncservice] ,
 * 内容是:[{"method":"thing.service.asyncservice","id":"23369371","params":{},"version":"1.0.0"}],
 * { "id": "123", "version": "1.0", "params": { "Power": "on", "WF": "2" },
 * "method": "thing.service.{tsl.service.identifier}" }
 *
 * @author zhensheng.cai on 2018/7/3.
 */
public class ServiceInvocationCommand extends BaseMqttCommand<ServiceInvocationReply> {
    private static final long serialVersionUID = -6852970783309556308L;
    private static Pattern pattern = Pattern.compile(ArrivedTopicPattern.SERVICE_INVOKE_COMMAND);

    @Override
    public Class<ServiceInvocationReply> getAnswerType() {
        return ServiceInvocationReply.class;
    }

    @Override
    public Pattern getMatchTopicPattern() {
        return pattern;
    }

    @Override
    public List<String> match(String topic) {
        //FIXME  topic match conflict with MeasurepointSet & MeasurepointGet
        Matcher matcher = this.getMatchTopicPattern().matcher(topic);
        if (matcher.matches()) {
            String[] groups = new String[matcher.groupCount()];
            for (int i = 0; i < matcher.groupCount(); i++) {
                groups[i] = matcher.group(i + 1);
            }
            List<String> topicArgs = Arrays.asList(groups);
            if (topicArgs.size() == 3 &&
                    ("measurepoint/set".equals(topicArgs.get(2)) ||
                            "measurepoint/get".equals(topicArgs.get(2)))) {
                return null;
            }
            return topicArgs;
        }

        return null;
    }
}
