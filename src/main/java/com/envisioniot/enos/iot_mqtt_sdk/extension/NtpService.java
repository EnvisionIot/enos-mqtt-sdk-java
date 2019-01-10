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
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * https://tools.ietf.org/html/rfc1305
 * @author zhensheng.cai
 * @date 2019/1/2.
 */
public class NtpService {

    private static Logger logger = LoggerFactory.getLogger(NtpService.class);

    private String ntpServer = "cn.pool.ntp.org";

    private int interval = 1;
    private TimeUnit timeUnit = TimeUnit.DAYS;
    private int timeout = 3000;
    private TimeInfo timeInfo;

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

    private void setTimeout(int timeout){
        this.timeout = timeout;
    }


    public NtpService() {
        sync();
    }

    public void sync() {
        try {
            this.timeInfo = pollNTPTime();
            scheduler.schedule(new PullTask(this), interval, timeUnit);
        } catch (Exception e) {
            logger.error("", e);
        }
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
            try {
                InetAddress hostAddr = InetAddress.getByName(this.ntpServer);
                System.out.println("> " + hostAddr.getHostName() + "/" + hostAddr.getHostAddress());
                TimeInfo info = client.getTime(hostAddr,NtpV3Packet.NTP_PORT);
                info.computeDetails();
                System.out.println(info);
                return info;

            } catch (IOException ioe) {
                ioe.printStackTrace();
                throw ioe;
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
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
    public long getFixedTimestmap(){
        if (timeInfo == null || timeInfo.getOffset() == null) {
            return System.currentTimeMillis();
        }
        return 0;
    }



    public static void main(String[] args) {
        MqttClient client = new MqttClient(new FileProfile());
        System.out.println(client.getExtServiceFactory().getNtpService().getFixedTimestmap());

    }

}
