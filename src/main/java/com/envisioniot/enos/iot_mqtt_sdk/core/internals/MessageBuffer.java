package com.envisioniot.enos.iot_mqtt_sdk.core.internals;

import com.envisioniot.enos.iot_mqtt_sdk.core.msg.IMqttDeliveryMessage;
import com.envisioniot.enos.iot_mqtt_sdk.core.msg.IMqttMessage;
import com.envisioniot.enos.iot_mqtt_sdk.core.msg.IMqttRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * msg request accumulator and disconnected msg buffer
 * @author zhensheng.cai
 * @date 2019/1/8.
 */
public class MessageBuffer {

    private static Logger logger = LoggerFactory.getLogger(MessageBuffer.class);

    private static final int defaultQueueSize = 5000;

    private final Object lockObj = new Object();

    private final Map<String, BlockingQueue<IMqttDeliveryMessage>> msgQueues = new ConcurrentHashMap<>();

    private final BlockingQueue<IMqttDeliveryMessage> disconnectedQueue = new LinkedBlockingQueue<>(defaultQueueSize);

    private MqttConnection connection;

    public MessageBuffer setConnection(MqttConnection connection) {
        this.connection = connection;
        return this;
    }

    public void putMessage(IMqttDeliveryMessage message){
        internalPutMessage(message.getMessageTopic(), message);
    }

    private int disconnetMessageCount(){
        synchronized (lockObj){
            return disconnectedQueue.size();
        }
    }

    public void putDisconnetedMessage(IMqttDeliveryMessage message){
        disconnectedQueue.offer(message);
    }

    private void internalPutMessage(String topic ,  IMqttDeliveryMessage message){
        BlockingQueue<IMqttDeliveryMessage> queue = msgQueues.get(topic);
        if(queue == null){
            synchronized (msgQueues){
                queue = msgQueues.computeIfAbsent(topic, k -> new LinkedBlockingQueue<>(defaultQueueSize));
            }
        }
        queue.offer(message);
    }



    public Runnable createRepublishDisconnetedMessageTask(){
        return new Runnable() {
            @Override
            public void run() {
                while (disconnectedQueue.peek() != null) {
                    try {
                        IMqttDeliveryMessage message = disconnectedQueue.take();
                        connection.fastPublish(message);
                    }
                    catch (Exception e ){
                        logger.error("", e);
                    }
                }
            }
        };
    }

//    public List<IMqttDeliveryMessage> drain(){
//
//    }

    




}
