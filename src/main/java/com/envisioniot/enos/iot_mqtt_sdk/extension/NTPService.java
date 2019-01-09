package com.envisioniot.enos.iot_mqtt_sdk.extension;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.NtpV3Packet;
import org.apache.commons.net.ntp.TimeInfo;

import java.io.IOException;
import java.net.InetAddress;

/**
 * @author zhensheng.cai
 * @date 2019/1/2.
 */
public class NTPService {


    private String ntpServer = "cn.pool.ntp.org";



    private int timeout = 3000;


    public void setNtpServer(String ntpServer) {
        this.ntpServer = ntpServer;
    }

    private void setTimeout(int timeout){
        this.timeout = timeout;
    }

    public void sync() {
        try {
            pollNTPTime();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void syncAtFixRate() {

    }


    private TimeInfo pollNTPTime() throws IOException {
        NTPUDPClient client = new NTPUDPClient();
        // We want to timeout if a response takes longer than 10 seconds
        client.setDefaultTimeout(this.timeout);
        try {
//            client.open();
            try {
                InetAddress hostAddr = InetAddress.getByName(this.ntpServer);
                System.out.println("> " + hostAddr.getHostName() + "/" + hostAddr.getHostAddress());
                TimeInfo info = client.getTime(hostAddr,NtpV3Packet.NTP_PORT);
                info.computeDetails();

                System.out.println(info);
                return info;
//                info.
//                processResponse(info);
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


    private void syncTimeIfNeeded(TimeInfo info){

    }

    public static void main(String[] args) {
        NTPService service = new NTPService();
        service.sync();

    }

}
