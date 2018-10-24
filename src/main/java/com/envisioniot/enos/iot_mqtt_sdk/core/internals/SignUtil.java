package com.envisioniot.enos.iot_mqtt_sdk.core.internals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SignUtil {

    public static final String hmacsha1 = "hmacsha1";

    public static String sign(String secret, Map<String, String> paramMap) {
        // 拼接有序的参数名-值串
        StringBuilder stringBuilder = new StringBuilder();
        if (paramMap != null) {
            // 对参数名进行字典排序
            String[] keyArray = paramMap.keySet().toArray(new String[0]);
            Arrays.sort(keyArray);
            for (String key : keyArray) {
                stringBuilder.append(key).append(paramMap.get(key));
            }
        }

        stringBuilder.append(secret);
        String codes = stringBuilder.toString();

        // SHA-1编码， 这里使用的是Apache
        // codec，即可获得签名(shaHex()会首先将中文转换为UTF8编码然后进行sha1计算，使用其他的工具包请注意UTF8编码转换)
        /*
		 * 以下sha1签名代码效果等同 byte[] sha = org.apache.common.codec.digest.DigestUtils
		 * .sha(org.apache.common.codec .binary.StringUtils.getBytesUtf8(codes)); String
		 * sign = org.apache.common
		 * .codec.binary.Hex.encodeHexString(sha).toUpperCase();
		 */
        //
        String sign = org.apache.commons.codec.digest.DigestUtils.sha1Hex(codes).toUpperCase();

        return sign;
    }

    public static void main(String[] args) {
        String a = sign("b", null);
        System.out.println(a);

        Map<String, String> map = new HashMap<String, String>();
        map.put("A", "1");
        map.put("B", "3");
        map.put("a", "4");
        String b = sign("b", map);
        System.out.println(b);
    }
}
