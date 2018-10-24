package com.envisioniot.enos.iot_mqtt_sdk.core.internals;

import com.envisioniot.enos.iot_mqtt_sdk.core.IConnectCallback;
import com.envisioniot.enos.iot_mqtt_sdk.core.IResponseCallback;
import com.envisioniot.enos.iot_mqtt_sdk.core.exception.EnvisionError;
import com.envisioniot.enos.iot_mqtt_sdk.core.exception.EnvisionException;
import com.envisioniot.enos.iot_mqtt_sdk.core.msg.*;
import com.envisioniot.enos.iot_mqtt_sdk.core.msg.IMqttArrivedMessage.DecodeResult;
import com.envisioniot.enos.iot_mqtt_sdk.core.profile.Profile;
import com.envisioniot.enos.iot_mqtt_sdk.message.downstream.BaseMqttReply;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author zhensheng.cai
 * @date 2018/7/5.
 */
public class DefaultProcessor implements MqttCallback, MqttCallbackExtended {
    private static Logger logger = LoggerFactory.getLogger(DefaultProcessor.class);
    private MqttClient mqttClient;
    private SubTopicCache subTopicCache;
    /**
     * response execution pool
     */
    private ExecutorService executor;

    /**
     * callback timeout pool
     */
    private ScheduledExecutorService timeoutScheduler = new ScheduledThreadPoolExecutor(1,
            new ThreadFactoryBuilder().setNameFormat("callback-timeout-pool-%d").build());


    public DefaultProcessor(MqttClient mqttClient, Profile profile, SubTopicCache subTopicCache) {
        this.mqttClient = mqttClient;
        this.executor = profile.getExecutorService();
        this.subTopicCache = subTopicCache;
    }

    private final Map<String, Task<? extends IMqttResponse>> rspTaskMap = new ConcurrentHashMap<>();
    private final Map<Class<? extends IMqttArrivedMessage>, IMessageHandler<?, ?>> arrivedMsgHandlerMap = new ConcurrentHashMap<>();
    private IConnectCallback connectCallback = null;

    public void onConnectFailed(int reasonCode) {
        if (connectCallback != null) {
            this.executor.execute(() -> connectCallback.onConnectFailed(reasonCode));
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("{} , {}", topic, mqttMessage);
            }

            DecodeResult result = null;
            List<IMqttArrivedMessage> decoderList = DecoderRegistry.getDeocderList();
            for (IMqttArrivedMessage decoder : decoderList) {
                result = decoder.decode(topic, mqttMessage.getPayload());
                if (result != null) {
                    break;
                }
            }

            // 1. decoder
            if (result == null) {
                logger.error("decode the rcv message failed , from topic {}", topic);
                return;
            }

            IMqttArrivedMessage msg = result.getArrivedMsg();
            if (msg == null) {
                logger.error("decode msg failed , from topic{} ", topic);
                return;
            }


            // 2. handle the msg
            if (msg instanceof IMqttResponse) {
                IMqttResponse mqttRsp = (IMqttResponse) msg;

                @SuppressWarnings("unchecked")
                Task<IMqttResponse> task = (Task<IMqttResponse>) rspTaskMap.remove(topic + "_" + mqttRsp.getMessageId());
                if (task == null) {
                    logger.error("no request answer the response , topic {}  ,msg {} ", topic, msg);
                    return;
                }

                task.run(mqttRsp);
            }

            @SuppressWarnings("unchecked") final IMessageHandler<IMqttArrivedMessage, IMqttDeliveryMessage> handler = (IMessageHandler<IMqttArrivedMessage, IMqttDeliveryMessage>) arrivedMsgHandlerMap.get(msg.getClass());
            final List<String> pathList = result.getPathList();
            if (handler != null) {
                executor.execute(() -> {
                    try {
                        IMqttDeliveryMessage deliveryMsg = handler.onMessage(msg, pathList);
                        if (deliveryMsg != null) {
                            deliveryMsg.setMessageId(msg.getMessageId());
                            deliveryMsg.setProductKey(msg.getProductKey());
                            deliveryMsg.setDeviceKey(msg.getDeviceKey());
                            /*set the reply topic*/
                            if (deliveryMsg instanceof BaseMqttReply) {
                                ((BaseMqttReply) deliveryMsg).setTopicArgs(pathList);
                                try {
                                    mqttClient.publish(deliveryMsg.getMessageTopic(), deliveryMsg.encode(), deliveryMsg.getQos(), false);
                                } catch (Exception e) {
                                    logger.error(
                                            "mqtt client publish reply msg to cloud failed ,arrived msg {}  msg to send {} ,  ",
                                            msg, deliveryMsg, e);
                                }
                            }
                        }

                    } catch (Exception e) {
                        logger.error(
                                "handle the arrived msg err , may because of registered arrived msg callback ,  ",
                                e);
                    }
                });
            } else {
            }
        } catch (Exception e) {
            logger.error("UGLY INTERNAL ERR!! , processing the arrived  msg err , topic {}  uncaught exception : ",
                    topic, e);
        }
    }


    public <T extends IMqttResponse> void doFastPublish(IMqttRequest<T> request) throws EnvisionException {
        try {
            request.check();
            mqttClient.publish(request.getMessageTopic(), request.encode(), request.getQos(), false);
        } catch (MqttException e) {
            logger.error("publish message failed messageRequestId {} ", request.getMessageTopic());
            throw new EnvisionException(e.getMessage(), e.getCause(), EnvisionError.MQTT_CLIENT_PUBLISH_FAILED);
        }
    }

    public <T extends IMqttResponse> void createCallbackTask(IMqttRequest<T> request, final IResponseCallback<T> callback, long timeout) throws EnvisionException {
        /*do expire*/
        if (callback != null) {
            final Task<T> task = new Task<T>();
            String key = request.getAnswerTopic() + "_" + request.getMessageId();
            Boolean isTimeout = true;

            FutureTask<Boolean> futureTask = new FutureTask<Boolean>(() -> {
                logger.warn("callback task timeout {} ", key);
                rspTaskMap.remove(key);
            }, isTimeout);
            task.setRunable(() -> {
                callback.onResponse(task.rsp);
                if (!futureTask.isDone()) {
                    futureTask.cancel(false);
                }
            });
            timeoutScheduler.schedule(futureTask, timeout, TimeUnit.MILLISECONDS);
            rspTaskMap.put(key, task);
        }

        this.doFastPublish(request);

    }

    <T extends IMqttResponse> FutureTask<T> createFutureTask(IMqttRequest<T> request) throws EnvisionException {
        String key = request.getAnswerTopic() + "_" + request.getMessageId();
        Task<T> task = new Task<>();
        FutureTask<T> future = new FutureTask<>(task);
        task.setRunable(future);
        rspTaskMap.put(key, task);
        this.doFastPublish(request);
        return future;
    }


    <T extends IMqttResponse> void removeFutureTask(IMqttRequest<T> request) {
        String key = request.getAnswerTopic() + "_" + request.getMessageId();
        rspTaskMap.remove(key);
    }


    public void setArrivedMsgHandler(Class<? extends IMqttArrivedMessage> arrivedMsgCls, IMessageHandler<?, ?> handler) {
        arrivedMsgHandlerMap.put(arrivedMsgCls, handler);
    }

    public void setConnectCallback(IConnectCallback callback) {
        connectCallback = callback;
    }

    public void removeArrivedMsgHandler(String topic) {
        arrivedMsgHandlerMap.remove(topic);
    }

    @Override
    public void connectionLost(Throwable throwable) {

        if (logger.isDebugEnabled()) {
            logger.debug("", throwable);
        }
        logger.error("Client <{}> Connection Lost ", this.mqttClient.getClientId());

        logger.info("clear the subscriptions");
        //无论怎样都对cache清空
        this.subTopicCache.clean();
        if (connectCallback != null) {
            try {
                this.executor.execute(() -> connectCallback.onConnectLost());
            } catch (Exception e) {
                logger.error("Connect Callback on connect lost , execution failed ,", e);
            }
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        if (logger.isDebugEnabled()) {
            logger.debug("delivery complete");
        }
    }

    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        if (logger.isDebugEnabled()) {
            logger.debug("connect complete , reconnect {} , serverUri {} ", reconnect, serverURI);
        }
        if (connectCallback != null) {
            try {
                this.executor.execute(() -> connectCallback.onConnectSuccess());
            } catch (Exception e) {
                logger.error("connectCallback on connect success execute failed  ", e);
            }
        }
    }

    //private static class Task

    private static class Task<T extends IMqttResponse> implements Callable<T> {
        private volatile T rsp;
        private Runnable runable;

        public void setRunable(Runnable runable) {
            this.runable = runable;
        }

        public void run(T rsp) {
            this.rsp = rsp;
            runable.run();
        }

        @Override
        public T call() throws Exception {
            return rsp;
        }
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
        final FutureTask<String> future = new FutureTask<>(new Callable<String>() {

            @Override
            public String call() throws Exception {
                return "abc";
            }
        });

        new Thread() {
            public void run() {
                try {
                    Thread.sleep(1000 * 2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                future.run();
            }
        }.start();


        new Thread() {
            public void run() {
                try {
                    Thread.sleep(1000 * 5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!future.isDone()) {
                    future.cancel(false);
                    System.out.println("canneled ");
                }
            }
        }.start();
        try {
            System.out.println("hello");
            System.out.println(future.get(10, TimeUnit.SECONDS));
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (true) {
            System.out.println("----------------------------------");
            Thread.sleep(1000);
        }
    }
}
