package com.envisioniot.enos.iot_mqtt_sdk.message.upstream.tsl;

import com.envisioniot.enos.iot_mqtt_sdk.core.internals.constants.ArrivedTopicPattern;
import com.envisioniot.enos.iot_mqtt_sdk.message.upstream.BaseMqttResponse;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * 错误码 消息 描述 460 request parameter error 请求参数错误 6321 tsl: device not exist in
 * product 设备不存在
 * AckMessageBody{"tslModelId":"Z9IKonoq","tslModelName":"zscai_test","tslModelDesc":"1111111111","tslModelCatagory":"zscai_test","tslAttributeMap":{"test":{"minimum":-2.147483648E9,"maximum":2.147483647E9,"exclusiveMinimum":false,"exclusiveMaximum":false,"defaultValue":1.0,"unit":{"unitId":"rad/s","multiplier":"ONE"},"hasQuality":false,"name":"test","desc":"1","required":false,"accessMode":true,"tags":{"tagMap":{}},"identifier":"test"}},"tslMeasurepointMap":{},"tslServiceMap":{"service2":{"outputData":[],"inputData":[],"name":"service2","required":false,"callType":"SYNC","desc":"test","identifier":"service2"},"service1":{"outputData":[],"inputData":[{"minimum":-2.147483648E9,"maximum":2.147483647E9,"exclusiveMinimum":false,"exclusiveMaximum":false,"unit":{"multiplier":"ONE"},"hasQuality":false,"name":"test","desc":"","required":false,"accessMode":false,"tags":{"tagMap":{}},"identifier":"test"}],"name":"service1","required":false,"callType":"ASYNC","desc":"","identifier":"service1"}},"tslEventMap":{},"tag":{"tagMap":{}},"allowAdditionalAttribute":false,"inheritedAttributeIds":[],"inheritedMeasurepointIds":[],"inheritedServiceIds":[],"inheritedEventIds":[],"extraInfo":{"createBy":"yz","createTime":1.534318625917E12,"updateBy":"customer_20180222","updateTime":1.534318748279E12,"ns":"test_orgCode"}}
 *
 * @author zhensheng.cai
 * @date 2018/7/12.
 */
public class TslTemplateGetResponse extends BaseMqttResponse {

    private static final long serialVersionUID = -5129474304666285214L;
    private static Pattern pattern = Pattern.compile(ArrivedTopicPattern.TSL_TEMPLATE_GET_REPLY);


    @SuppressWarnings("unchecked")
    public Map<String, Object> getAttributs() {
        Map<String, Object> data = this.getData();
        return ((Map<String, Object>) data.get("tslAttributeMap"));
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getMeasurepoints() {
        Map<String, Object> data = this.getData();
        return ((Map<String, Object>) data.get("tslMeasurepointMap"));
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getEvents() {
        Map<String, Object> data = this.getData();
        return ((Map<String, Object>) data.get("tslEventMap"));
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getServices() {
        Map<String, Object> data = this.getData();
        return ((Map<String, Object>) data.get("tslServiceMap"));
    }

    @Override
    public Pattern getMatchTopicPattern() {
        return pattern;
    }


}

/*
 * { "id": "123", "code": 200, "data": { "schema":
 * "https://iot-tsl.oss-cn-shanghai.aliyuncs.com/schema.json", "link":
 * "/sys/1234556554/airCondition/thing/", "profile": { "productKey":
 * "1234556554", "deviceName": "airCondition" }, "properties": [ { "identifier":
 * "fan_array_property", "name": "风扇数组属性", "accessMode": "r", "required": true,
 * "dataType": { "type": "array", "specs": { "size": "128", "item": { "type":
 * "int" } } } } ], "events": [ { "identifier": "alarm", "name": "alarm",
 * "desc": "风扇警报", "type": "alert", "required": true, "outputData": [ {
 * "identifier": "errorCode", "name": "错误码", "dataType": { "type": "text",
 * "specs": { "length": "255" } } } ], "method": "thing.event.alarm.post" } ],
 * "services": [ { "identifier": "timeReset", "name": "timeReset", "desc":
 * "校准时间", "inputData": [ { "identifier": "timeZone", "name": "时区", "dataType":
 * { "type": "text", "specs": { "length": "512" } } } ], "outputData": [ {
 * "identifier": "curTime", "name": "当前的时间", "dataType": { "type": "date",
 * "specs": {} } } ], "method": "thing.service.timeReset" }, { "identifier":
 * "set", "name": "set", "required": true, "desc": "属性设置", "method":
 * "thing.service.property.set", "inputData": [ { "identifier":
 * "fan_int_property", "name": "风扇整数型属性", "accessMode": "rw", "required": true,
 * "dataType": { "type": "int", "specs": { "min": "0", "max": "100", "unit":
 * "g/ml", "unitName": "毫升" } } } ], "outputData": [] }, { "identifier": "get",
 * "name": "get", "required": true, "desc": "属性获取", "method":
 * "thing.service.property.get", "inputData": [ "array_property",
 * "fan_int_property", "batch_enum_attr_id", "fan_float_property",
 * "fan_double_property", "fan_text_property", "fan_date_property",
 * "batch_boolean_attr_id", "fan_struct_property" ], "outputData": [ {
 * "identifier": "fan_array_property", "name": "风扇数组属性", "accessMode": "r",
 * "required": true, "dataType": { "type": "array", "specs": { "size": "128",
 * "item": { "type": "int" } } } } ] } ] } }
 */