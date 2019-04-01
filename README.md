# Using EnOS Device SDK for MQTT for Java

This article instructs how to prepare your development environment to use the *EnOS Device SDK for MQTT for Java*.

Preparing development environment
- [Installing Java JDK SE](#installjava)
- [Installing Maven](#installmaven)
- Obtaining EnOS Device SDK for MQTT for Java
  - [Including Dependency in Maven Project](#includedependency)
  - [Building from Source Code](#buildfromsource)
- [Feature List](#featurelist)
- [Sample Code](#samplecode)

<a name="#installjava"></a>
## Installing Java JDK SE

To use the EnOS Device SDK for MQTT for Java, you will need to install **Java SE 8**.

<a name="#installmaven"></a>
## Installing Maven

To use EnOS Device SDK for MQTT for Java, we recommend you use **Maven 3**.

## Obtaining EnOS Device SDK for MQTT for Java

You can obtain the EnOS Device SDK for MQTT for Java through the following methods:

- Include the project as a dependency in your Maven project
- Download the source code by cloning this repo and build on your machine

<a name="#includedependency"></a>
### Including Dependency in Maven Project

*This is the recommended method of including the EnOS IoT SDKs in your project.*

- Navigate to [http://search.maven.org](http://search.maven.org/), search for **com.envisioniot.enos** and take note of the latest version number (or the version number of whichever version of the sdk you desire to use).

- In your main pom.xml file, add the EnOS Device SDK for MQTT for Java as a dependency as follows:

  ```
  <dependency>
      <groupId>com.envisioniot</groupId>
      <artifactId>enos-mqtt</artifactId>
      <version>2.1.0</version>
      <!--You might need to change the version number as you need.-->
  </dependency>
  ```

<a name="#buildfromsource"></a>
### Building from Source Code

- Get a copy of the **EnOS Device SDK for MQTT for Java** from master branch of the GitHub (current repo). You should fetch a copy of the source from the **master** branch of the GitHub repository: <https://github.com/EnvisionIot/enos-iot-mqtt-java-sdk>

  ```
  git clone https://github.com/EnvisionIot/enos-iot-mqtt-java-sdk.git
  ```

- When you have obtained a copy of the source, you can build the SDK for Java.

<a name="#featurelist"></a>
## Key Features

The EnOS Device SDK for MQTT for Java supports the following functions:

- Registration of devices
- Add, update, query, or delete of gateway devices
- Online and offline of sub-devices
- Create or delete of device tags
- Create device measurepoints
- Upload device alerts 
- Upload device messages
- Set device measurepoints and get measurepoint data
- Enable device services
- Send messages on device startup, stop, and delete

<a name="#samplecode"></a>
## Sample Code
- [Connect to EnOS Cloud](https://www.envisioniot.com/docs/device-connection/en/latest/howto/device/develop/java/connect.html)
- [Connect to EnOS via SSL/TLS](https://www.envisioniot.com/docs/device-connection/en/latest/howto/device/develop/java/connect_ssl.html)
- [Connect via Configuration Profile](https://www.envisioniot.com/docs/device-connection/en/latest/howto/device/develop/java/connect_viaprofile.html)
- [Send Device Telemetry to EnOS](https://www.envisioniot.com/docs/device-connection/en/latest/howto/device/develop/java/post_data_to_cloud.html)
- [Send Command to Device](https://www.envisioniot.com/docs/device-connection/en/latest/howto/device/develop/java/send_command_to_device.html)
- [NTP Service](https://www.envisioniot.com/docs/device-connection/en/latest/howto/device/develop/java/ntp_service.html)
- [OTA Service](https://www.envisioniot.com/docs/device-connection/en/latest/howto/device/develop/java/ota_service.html)
- End-to-End Sample Code:
  You can find the sample code from `<dir>/blob/master/src/main/java/com/envisioniot/enos/iot_mqtt_sdk/sample/SimpleSendReceive.java` , where, `<dir>` is the directory of this SDK in your local store.

