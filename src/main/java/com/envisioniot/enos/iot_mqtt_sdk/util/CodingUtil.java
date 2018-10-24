package com.envisioniot.enos.iot_mqtt_sdk.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class CodingUtil {
    public static final String DEFAULT_ENCODE = "utf-8";
    public static final int UNSIGNEDINT_MAX = 1073741823;

    public static void paddingBytes(ByteArrayOutputStream bos, byte[] data) {
        if (data == null || data.length == 0) {
            return;
        }

        try {
            // 写len
            bos.write(encodeUnsignInt(data.length));
            // 写内容
            bos.write(data);
        } catch (IOException e) {// 不可能到这里
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void paddingInt(ByteArrayOutputStream bos, int data) {
        paddingBytes(bos, encodeInt(data));
    }

    public static void paddingUnsignedInt(ByteArrayOutputStream bos, int data) {
        paddingBytes(bos, encodeUnsignInt(data));
    }

    public static void paddingString(ByteArrayOutputStream bos, String data) {
//        paddingUnsignedInt(bos, data.length());
        paddingBytes(bos, data.getBytes());
    }


    public static byte[] encodeBytes(byte[] data) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        // null或者空串,直接用0表示
        if (data == null || data.length == 0) {
            return new byte[]{0};
        }

        try {
            // 写len
            bos.write(encodeUnsignInt(data.length));
            // 写内容
            bos.write(data);
        } catch (IOException e) {// 不可能到这里
            throw new RuntimeException(e.getMessage());
        }
        return bos.toByteArray();
    }

    public static byte[] decodeBytes(byte[] data) {
        return readBytes(ByteBuffer.wrap(data));
    }

    public static byte[] readBytes(ByteBuffer bf) {
        int len = readUnsignInt(bf);
        byte[] data = new byte[len];
        bf.get(data);
        return data;
    }

    public static byte[] encodeString(String k) {
        if (k == null || k.length() == 0) return new byte[]{0};
        else {
            try {
                return encodeBytes(k.getBytes(DEFAULT_ENCODE));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("UnsupportedEncoding " + DEFAULT_ENCODE + ", encodeString fail.");
            }
        }
    }

    public static String readString(ByteBuffer bf) {
        try {
            return new String(readBytes(bf), DEFAULT_ENCODE);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UnsupportedEncoding " + DEFAULT_ENCODE + ", readString fail.");
        }
    }

    public static String decodeString(byte[] data) {
        return readString(ByteBuffer.wrap(data));
    }

    public static byte[] encodeUnsignInt(int value) {// 前2bit表示要用几个byte存储,只能存非负数,存储最大值1,073,741,824
        int len = 0;
        if (value < 0) {
            throw new RuntimeException("Value must > 0.");
        } else if (value <= 63) {// 用1字节存
            len = 1;
        } else if (value <= 16383) {// 用2字节存
            len = 2;
        } else if (value <= 4194303) {// 用3字节存
            len = 3;
        } else if (value <= 1073741823) {// 用4字节存
            len = 4;
        } else {// 超出可存储最大值
            throw new RuntimeException("Value too large!");
        }

        byte[] data = new byte[len];
        for (int i = 0; i < len; i++) {
            data[len - i - 1] = (byte) (value >> 8 * i & 0xFF);
        }
        data[0] |= (len - 1) << 6;
        return data;
    }

    public static int readUnsignInt(ByteBuffer bf) {
        int result = 0;
        byte b = bf.get();
        int len = (b & 0xC0) >>> 6;
        // System.out.println("len:"+ len +" "+ b);
        b &= 0x3F;
        for (int i = len; ; i--) {
            result += (b & 0xFF) << (8 * i);

            if (i == 0) break;
            else b = bf.get();
        }
        return result;
    }

    public static int decodeUnsignInt(byte[] data) {
        int result = 0;
        int len = (data[0] & 0xC0) >>> 6;
        // System.out.println("len:"+ len +" "+ data[0]);
        data[0] &= 0x3F;
        byte b;
        for (int i = 0; i <= len; i++) {
            b = data[len - i];
            result += (b & 0xFF) << (8 * i);
        }
        return result;
    }

    //将无符号int转换为long，专为QQ号码21亿切换的工具
    public static int uin21LongToInt(long uinL) {
        return (int) uinL;
    }

    public static List<Integer> uin21LongToInt(List<Long> uins) {
        List<Integer> intList = new ArrayList<Integer>();
        for (long uin : uins) {
            intList.add(uin21LongToInt(uin));
        }
        return intList;
    }

    //将long转化为无符号的int，专为QQ号码21亿切换的工具
    public static long uin21IntToLong(int uin) {
        return uin & 0xFFFFFFFFL;
    }

    public static List<Long> uin21IntToLong(List<Integer> uins) {
        List<Long> longList = new ArrayList<Long>();
        for (int uin : uins) {
            longList.add(uin21IntToLong(uin));
        }
        return longList;
    }

    public static byte[] encodeInt(int v) {// 前3bit表示要用几个byte存储,第4bit表示符号
        int flag = v < 0 ? 1 : 0;
        int value = Math.abs(v);// Integer.MIN_VALUE要特殊处理,用-0表示
        // System.out.println(value);
        int len = value <= 15 ? 1 : value <= 4095 ? 2 : value <= 1048575 ? 3 : value <= 268435455 ? 4 : 5;
        byte[] data = new byte[len];
        for (int i = 0; i < len && i < 4; i++) {
            data[len - i - 1] = (byte) (value >> 8 * i & 0xFF);
        }
        data[0] |= (((len - 1) << 1) + flag) << 4;
        return data;
    }

    public static int decodeInt(byte[] data) {
        int result = 0;
        int len = (data[0] & 0xE0) >>> 5;
        // System.out.println("len:"+ len +" "+ data[0]);
        boolean flag = ((data[0] & 0x10) != 0);
        data[0] &= 0x0F;
        byte b;
        for (int i = 0; i <= len & i < 4; i++) {
            b = data[len - i];
            result += (b & 0xFF) << (8 * i);
        }
        if (flag) {
            if (result == 0) result = Integer.MIN_VALUE;
            else result *= -1;
        }
        return result;
    }

    public static int readInt(ByteBuffer bf) {
        int result = 0;
        byte b = bf.get();
        // System.out.println("b::"+ b);
        int len = (b & 0xE0) >>> 5;

        boolean flag = ((b & 0x10) != 0);
        b &= 0x0F;
        for (int i = len; ; i--) {
            result += (b & 0xFF) << (8 * i);

            if (i == 0) break;
            else b = bf.get();
        }
        if (flag) {
            if (result == 0) result = Integer.MIN_VALUE;
            else result *= -1;
        }
        return result;
    }

    public static long decodeLong(byte[] data) {
        long result = 0;
        int len = (data[0] & 0xF8) >>> 4;
        // System.out.println("len:"+ len +" "+ data[0]);
        boolean flag = ((data[0] & 0x08) != 0);
        data[0] &= 0x07;
        byte b;
        for (int i = 0; i <= len & i < 8; i++) {
            b = data[len - i];
            result += (long) (b & 0xFF) << (8 * i);
        }
        if (flag) {
            if (result == 0) result = Long.MIN_VALUE;
            else result *= -1;
        }
        return result;
    }

    public static long readLong(ByteBuffer bf) {
        long result = 0;
        byte b = bf.get();
        int len = (b & 0xF8) >>> 4;
        // System.out.println("len:"+ len +" "+ data[0]);
        boolean flag = ((b & 0x08) != 0);
        b &= 0x07;
        for (int i = len; ; i--) {
            result += (long) (b & 0xFF) << (8 * i);
            if (i == 0) break;
            else b = bf.get();
        }
        if (flag) {
            if (result == 0) result = Long.MIN_VALUE;
            else result *= -1;
        }
        return result;
    }

    public static byte[] encodeLong(long v) {// 4bit表示要用几个byte存储,第5bit表示符号
        int flag = v < 0 ? 1 : 0;
        long value = Math.abs(v);
        // System.out.println(value);
        int len = value <= 7L ? 1 : value <= 2047L ? 2 : value <= 524287L ? 3 : value <= 134217727L ? 4 : value <= 34359738367L ? 5 : value <= 8796093022207L ? 6 : value <= 2251799813685247L ? 7 : value <= 576460752303423487L ? 8 : 9;

        byte[] data = new byte[len];
        for (int i = 0; i < len && i < 8; i++) {
            data[len - i - 1] = (byte) (value >> 8 * i & 0xFF);
        }
        data[0] |= (((len - 1) << 1) + flag) << 3;
        return data;
    }

    public static boolean decodeBoolean(byte b) {
        return b != 0;
    }

    public static byte[] encodeBoolean(boolean value) {
        return new byte[]{value ? (byte) 1 : (byte) 0};
    }

    public static void main(String[] args) {
        /*
         * for (int i = 1; i <= 8; i++) { long aa = getMaxStoreValue_l(5,i);
         * System.out.print((aa-1) +"L,"+ aa +"L,"+ (aa+1) +"L,"); }
         */
        /*
         * long bb = System.currentTimeMillis(); for (int i = 0; i <= 1000000;
         * i++) { encodeString("stone"); ///long v = i + 100000000L;
         * //if(ByteUtil.debyteLong(ByteUtil.enbyteLong(v)) != v)
         * //if(decodeLong(encodeLong(v)) != v)
         * //if(decodeUnsignInt(encodeUnsignInt(i)) != i)
         * //if(decodeInt(encodeInt(i)) != i)
         * //if(ByteUtil.debyteInt(ByteUtil.enbyteInt(i)) != i) {
         * System.out.println(v); break; } }
         * System.out.println(System.currentTimeMillis() - bb);
         */
        /*
         * long bb = System.currentTimeMillis(); for (int i = 0; i < 4; i++) {
         * //System.out.println(getMaxStoreValue_l(4,i+1)); //int a = -1 >>> 18;
         * //getMaxStoreValue_i(2,4); }
         * System.out.println(System.currentTimeMillis() - bb);
         */
        long uinL = 2200000001L;
        System.out.println(uin21LongToInt(uinL));
        int uin = uin21LongToInt(uinL);
        System.out.println(uin21IntToLong(uin));
    }

}
