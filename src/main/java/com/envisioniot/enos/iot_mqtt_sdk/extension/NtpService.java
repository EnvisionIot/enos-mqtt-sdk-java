package com.envisioniot.enos.iot_mqtt_sdk.extension;

import com.envisioniot.enos.iot_mqtt_sdk.core.MqttClient;
import com.envisioniot.enos.iot_mqtt_sdk.core.profile.FileProfile;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.NtpV3Packet;
import org.apache.commons.net.ntp.TimeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.*;

/**
 * https://tools.ietf.org/html/rfc1305
 * @author zhensheng.cai
 * @date 2019/1/2.
 */
public class NtpService {

    private static Logger logger = LoggerFactory.getLogger(NtpService.class);
    private final ScheduledFuture<?> initFuture;

    private String ntpServer = "cn.pool.ntp.org";

    private int interval = 1;
    private TimeUnit timeUnit = TimeUnit.DAYS;
    private int timeout;
    private TimeInfo timeInfo;
    private int retries;



    private static ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(
            1, new ThreadFactoryBuilder().setNameFormat("ntp-service-scheduler").build(),
            new ThreadPoolExecutor.AbortPolicy()
    );

    public void setCheckInterval(int interval , TimeUnit timeUnit){
        this.interval = interval;
        this.timeUnit = timeUnit;
    }


    public void setNtpServer(String ntpServer) {
        this.ntpServer = ntpServer;
    }

    public void setTimeout(int timeout){
        this.timeout = timeout;
    }

    public void setRetries(int retires){
        this.retries = retires;
    }

    public NtpService() {
        this.initFuture = scheduler.schedule(new PullTask(this), 0, timeUnit);
    }

    public boolean sync() {
        try {
            this.timeInfo = pollNTPTime();
            return true;
        } catch (Exception e) {
            boolean result = retryRepeat(retries);
            if(result) {
                logger.error("", e);
            }
        }
        return false;
    }

    public boolean retryRepeat(int times){
        System.out.println("retry");
        while(times-- >0 ){
            try {
                this.timeInfo = pollNTPTime();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }



    public static class PullTask implements Runnable {

        final NtpService service;

        private PullTask(NtpService service) {
            this.service = service;
        }

        @Override
        public void run() {
            try {
                service.timeInfo = service.pollNTPTime();
            } catch (Exception e) {
                //if pull ntp time failed, use the last pulled timeInfo
            }
            scheduler.schedule(this, service.interval, service.timeUnit);
        }
    }



    private TimeInfo pollNTPTime() throws Exception {
        NTPUDPClient client = new NTPUDPClient();
        // We want to timeout if a response takes longer than 10 seconds
        client.setDefaultTimeout(this.timeout);
        try {

            InetAddress hostAddr = InetAddress.getByName(this.ntpServer);
            System.out.println("> " + hostAddr.getHostName() + "/" + hostAddr.getHostAddress());
            TimeInfo info = client.getTime(hostAddr,NtpV3Packet.NTP_PORT);
            info.computeDetails();
            System.out.println(info);
            return info;
        }
        finally {
            client.close();
        }
    }

    /**
     *
     * LocalClockOffset = ((ReceiveTimestamp_ntp - OriginateTimestamp_local) +(TransmitTimestamp_ntp - DestinationTimestamp_local)) / 2
     * offset =
     * Where OriginateTimestamp is the local time the client sent the packet (t1), ReceiveTimestamp is time request received by NTP server (t2), TransmitTimestamp is time reply sent by server (t3), and DestinationTimestamp is time at which reply received by client on local machine (t4).
     * @return
     */
    public long getFixedTimestamp(){
        check();
        return System.currentTimeMillis()+ timeInfo.getOffset();
    }


    public long getFixedTimestamp(long timestamp){
        check();
        return timestamp+ timeInfo.getOffset();
    }


    public void check(){
        if (timeInfo == null || timeInfo.getOffset() == null) {
            try {
                //get the first pull result
                initFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                logger.error("", e);
            }
            if (timeInfo == null || timeInfo.getOffset() == null) {
                throw new UnsupportedOperationException("ntp time pull failed , cannot getFixedTimestamp");
            }
        }
    }


    public static void main(String[] args) {
        MqttClient client = new MqttClient(new FileProfile());
        System.out.println("local :" + System.currentTimeMillis());
        System.out.println("fix : " + (client.getExtServiceFactory().getNtpService().getFixedTimestamp()));
        System.out.println("fix : " + (client.getExtServiceFactory().getNtpService().getFixedTimestamp(System.currentTimeMillis())));
    }

}
