package com.envisioniot.enos.iot_mqtt_sdk.core.internals;

import com.envisioniot.enos.iot_mqtt_sdk.core.msg.IMqttArrivedMessage;
import com.envisioniot.enos.iot_mqtt_sdk.util.PackageScanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 做成静态工具类 ？？？ 本身应该只需要register一次
 *
 * @author zhensheng.cai
 * @date 2018/7/10.
 */
public class DecoderRegistry {

    public static final String DECODER_PACKAGE = "com.envisioniot.enos.iot_mqtt_sdk.message";

    private static final Logger logger = LoggerFactory.getLogger(DecoderRegistry.class);
    private static List<IMqttArrivedMessage> decoderList = new ArrayList<>();
    private static List<IMqttArrivedMessage> unmodefiDecoderList = Collections.unmodifiableList(decoderList);

    static {
        // 对于上行的response 可以动态加载
        // 只需要静态加载各类Command的的编码方式即可
        try {
            for (Class<?> clss : PackageScanUtil.scan(DECODER_PACKAGE, IMqttArrivedMessage.class)) {
                IMqttArrivedMessage decoder = (IMqttArrivedMessage) clss.newInstance();
                decoderList.add(decoder);
            }
        } catch (Exception e) {
            logger.error("register downstream command decoder failed ", e);
        }
    }

    public static List<IMqttArrivedMessage> getDecoderList() {
        return unmodefiDecoderList;
    }
}
