package com.envisioniot.enos.iot_mqtt_sdk.core.internals;

import java.util.concurrent.ConcurrentHashMap;

public class SubTopicCache {
    private ConcurrentHashMap<String, String> topicMap = new ConcurrentHashMap<>();

    public boolean exists(String topic) {
        return topicMap.containsKey(topic);
    }

    public void put(String topic) {
        topicMap.put(topic, "");
    }

    public void clean() {
        topicMap.clear();
    }
}
