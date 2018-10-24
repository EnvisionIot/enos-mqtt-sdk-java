package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.tsl;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.ArrivedTopicPattern;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.BaseMqttResponse;

import java.util.regex.Pattern;

/**
 * 460 request parameter error 请求参数错误 6106 map size must less than 200
 * 设备上报属性一次性最多只能上报200条属性 6313 tsl service not available
 * 用户上报属性时会进行校验，检查上报的属性是否符合用户定义的属性格式，当校验服务不可用时会报这个错。 属性校验参考定义物模型
 *
 * @author zhensheng.cai
 * @date 2018/7/12.
 */
public class MeasurepointPostResponse extends BaseMqttResponse {

    private static final long serialVersionUID = 1289287472462692026L;
    private static Pattern pattern = Pattern.compile(ArrivedTopicPattern.MEASUREPOINT_POST_REPLY);


    @Override
    public Pattern getMatchTopicPattern() {
        return pattern;
    }
}
