package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.tsl;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.ArrivedTopicPattern;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.BaseMqttResponse;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhensheng.cai
 * @date 2018/7/9.
 */
public class EventPostResponse extends BaseMqttResponse {
    private static final long serialVersionUID = -8771628509472624926L;
    private static Pattern pattern = Pattern.compile(ArrivedTopicPattern.EVENT_POST_REPLY);


    @Override
    public Pattern getMatchTopicPattern() {
        return pattern;
    }

    @Override
    public List<String> match(String topic) {
        Matcher matcher = this.getMatchTopicPattern().matcher(topic);
        if (matcher.matches()) {
            String[] groups = new String[matcher.groupCount()];
            for (int i = 0; i < matcher.groupCount(); i++) {
                groups[i] = matcher.group(i + 1);
            }
            List<String> topicArgs = Arrays.asList(groups);
            if (topicArgs.size() == 3 && "measurepoint".equals(topicArgs.get(2))) {
                return null;
            }
            return topicArgs;
        }

        return null;
    }


}
