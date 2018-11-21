package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.topo;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.ArrivedTopicPattern;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.BaseMqttResponse;

import java.util.regex.Pattern;

/**
 * Description: add topotaxy response
 *
 * @author zhonghua.wu
 * @create 2018-07-09 14:22
 */
public class TopoAddResponse extends BaseMqttResponse {
    private static final long serialVersionUID = -6597119960979344814L;
    private static Pattern pattern = Pattern.compile(ArrivedTopicPattern.TOPO_ADD_REPLY);

    @Override
    public Pattern getMatchTopicPattern() {
        return pattern;
    }
}
