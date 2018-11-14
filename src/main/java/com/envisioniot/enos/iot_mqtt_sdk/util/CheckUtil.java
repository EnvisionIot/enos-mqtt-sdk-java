package com.envisioniot.enos.iot_mqtt_sdk.util;

import com.envisioniot.enos.iot_mqtt_sdk.core.exception.EnvisionError;
import com.envisioniot.enos.iot_mqtt_sdk.core.exception.EnvisionException;

import java.util.Collection;
import java.util.Map;

/**
 * Description: check request valid
 *
 * @author zhonghua.wu
 * @create 2018-07-12 11:59
 */
public class CheckUtil {

    // check productKey 和 check deviceKey 很常用，这里特化了这两个check
    // specific check productKey
    public static void checkProductKey(String productKey) throws EnvisionException {
        checkStringNotEmpty(productKey, "productKey");
    }

    // specific check deviceKey
    public static void checkDeviceKey(String deviceKey) throws EnvisionException {
        checkStringNotEmpty(deviceKey, "deviceKey");
    }

    // check string, collection or map is not empty
    public static void checkNotEmpty(Object value, String fieldName) throws EnvisionException {
        checkNotNull(value, fieldName);
        if (value instanceof String) {
            checkStringNotEmpty((String) value, fieldName);
        }
        if (value instanceof Collection<?>) {
            checkCollectionNotEmpty((Collection<?>) value, fieldName);
        }
        if (value instanceof Map<?, ?>) {
            checkMapNotEmpty((Map<?, ?>) value, fieldName);
        }
    }

    public static void checkNotNull(Object value, String fieldName) throws EnvisionException {
        if (value == null) {
            throw new EnvisionException(EnvisionError.CODE_ERROR_MISSING_ARGS.getErrorCode(),
                    "sdk-client exception: " + fieldName + " is mandatory");
        }
    }

    // check string not empty
    public static void checkStringNotEmpty(String string, String fieldName) throws EnvisionException {
        if (StringUtil.isEmpty(string)) {
            throw new EnvisionException(EnvisionError.CODE_ERROR_MISSING_ARGS.getErrorCode(),
                    "sdk-client exception: " + fieldName + " is mandatory");
        }
    }

    // check collection not empty
    public static void checkCollectionNotEmpty(Collection<?> collection, String fieldName) throws EnvisionException {
        if (collection == null || collection.isEmpty()) {
            throw new EnvisionException(EnvisionError.CODE_ERROR_MISSING_ARGS.getErrorCode(),
                    "sdk-client exception: " + fieldName + " is mandatory");
        }
    }

    // check map not empty
    public static void checkMapNotEmpty(Map<?, ?> map, String fieldName) throws EnvisionException {
        if (map == null || map.isEmpty()) {
            throw new EnvisionException(EnvisionError.CODE_ERROR_MISSING_ARGS.getErrorCode(),
                    "sdk-client exception: " + fieldName + " is mandatory");
        }
    }

    // check string max length
    public static void checkMaxLength(String string, int maxLength, String fieldName) throws EnvisionException {
        if (string.length() > maxLength) {
            throw new EnvisionException(EnvisionError.CODE_ERROR_ARG_INVALID.getErrorCode(),
                    "sdk-client exception: " + fieldName + " is invalid, length cannot be large than " + maxLength);
        }
    }

    // check collection size max size
    public static void checkMaxSize(Collection<?> collection, int maxSize, String fieldName) throws EnvisionException {
        if (collection.size() > maxSize) {
            throw new EnvisionException(EnvisionError.CODE_ERROR_ARG_INVALID.getErrorCode(),
                    "sdk-client exception: " + fieldName + " is invalid, size cannot be large than " + maxSize);
        }
    }

    public static void main(String[] args) {
        System.out.println("fafc692c-fa9b-40e3-b4d8-0e8b4a53c32b".length());

    }
}
