package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.topo;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.ArrivedTopicPattern;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.BaseMqttResponse;

import java.util.regex.Pattern;

/**
 * Description: delete topotaxy response
 *
 * @author zhonghua.wu
 * @create 2018-07-09 14:26
 */
public class TopoDeleteResponse extends BaseMqttResponse {

    private static final long serialVersionUID = -6759065710108009661L;
    private static Pattern pattern = Pattern.compile(ArrivedTopicPattern.TOPO_DELETE_REPLY);

    @Override
    public Pattern getMatchTopicPattern() {
        return pattern;
    }
}
