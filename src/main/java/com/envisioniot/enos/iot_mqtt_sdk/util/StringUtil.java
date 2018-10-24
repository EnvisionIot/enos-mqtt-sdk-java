package com.envisioniot.enos.iot_mqtt_sdk.util;

import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

public class StringUtil {
    public static final String ENC_UTF8 = "UTF-8";
    public static final String ENC_GBK = "GBK";
    public static final Charset GBK = Charset.forName("GBK");
    public static final Charset UTF_8 = Charset.forName("UTF-8");

    public StringUtil() {
    }

    public static String formatDate(String dateStr, String inputFormat, String format) {
        String resultStr = dateStr;

        try {
            Date date = (new SimpleDateFormat(inputFormat)).parse(dateStr);
            resultStr = formatDate(date, format);
        } catch (ParseException var5) {
            ;
        }

        return resultStr;
    }

    public static String formatDate(String dateStr, String format) {
        String inputFormat = "yyyy-MM-dd HH:mm:ss";
        if (dateStr == null) {
            return "";
        } else {
            if (dateStr.matches("\\d{1,4}\\-\\d{1,2}\\-\\d{1,2}\\s+\\d{1,2}:\\d{1,2}:\\d{1,2}\\.\\d{1,3}")) {
                inputFormat = "yyyy-MM-dd HH:mm:ss.SSS";
            } else if (dateStr.matches("\\d{4}\\-\\d{1,2}\\-\\d{1,2} +\\d{1,2}:\\d{1,2}")) {
                inputFormat = "yyyy-MM-dd HH:mm:ss";
            } else if (dateStr.matches("\\d{4}\\-\\d{1,2}\\-\\d{1,2} +\\d{1,2}:\\d{1,2}")) {
                inputFormat = "yyyy-MM-dd HH:mm";
            } else if (dateStr.matches("\\d{4}\\-\\d{1,2}\\-\\d{1,2} +\\d{1,2}")) {
                inputFormat = "yyyy-MM-dd HH";
            } else if (dateStr.matches("\\d{4}\\-\\d{1,2}\\-\\d{1,2} +\\d{1,2}")) {
                inputFormat = "yyyy-MM-dd";
            } else if (dateStr.matches("\\d{1,4}/\\d{1,2}/\\d{1,2}\\s+\\d{1,2}:\\d{1,2}:\\d{1,2}\\.\\d{1,3}")) {
                inputFormat = "yyyy/MM/dd HH:mm:ss.SSS";
            } else if (dateStr.matches("\\d{4}/\\d{1,2}/\\d{1,2} +\\d{1,2}:\\d{1,2}")) {
                inputFormat = "yyyy/MM/dd HH:mm:ss";
            } else if (dateStr.matches("\\d{4}/\\d{1,2}/\\d{1,2} +\\d{1,2}:\\d{1,2}")) {
                inputFormat = "yyyy/MM/dd HH:mm";
            } else if (dateStr.matches("\\d{4}/\\d{1,2}/\\d{1,2} +\\d{1,2}")) {
                inputFormat = "yyyy/MM/dd HH";
            } else if (dateStr.matches("\\d{4}/\\d{1,2}/\\d{1,2} +\\d{1,2}")) {
                inputFormat = "yyyy/MM/dd";
            }

            String resultStr = formatDate(dateStr, inputFormat, format);
            return resultStr;
        }
    }

    public static String formatDate(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String encodeLocale(String locale) {
        String[] result = split(encodeSQL(locale), ".");
        return result.length > 0 ? replaceAll(result[0], "-", "_") : "";
    }

    public static String encodeSQL(String sql) {
        if (sql == null) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < sql.length(); ++i) {
                char c = sql.charAt(i);
                switch (c) {
                    case '\b':
                        sb.append("\\b");
                        break;
                    case '\t':
                        sb.append("\\t");
                        break;
                    case '\n':
                        sb.append("\\n");
                        break;
                    case '\r':
                        sb.append("\\r");
                        break;
                    case '"':
                        sb.append("\\\"");
                        break;
                    case '\'':
                        sb.append("''");
                        break;
                    case '\\':
                        sb.append("\\\\");
                    case '\u200b':
                    case '\ufeff':
                        break;
                    default:
                        sb.append(c);
                }
            }

            return sb.toString();
        }
    }

    public static String convertString(String str, String defaults) {
        return (String) MoreObjects.firstNonNull(str, defaults);
    }

    public static int convertInt(String str, int defaults) {
        return str == null ? defaults
                : ((Integer) MoreObjects.firstNonNull(Ints.tryParse(str), Integer.valueOf(defaults))).intValue();
    }

    public static long convertLong(String str, long defaults) {
        return str == null ? defaults
                : ((Long) MoreObjects.firstNonNull(Longs.tryParse(str), Long.valueOf(defaults))).longValue();
    }

    public static double convertDouble(String str, double defaults) {
        return str == null ? defaults
                : ((Double) MoreObjects.firstNonNull(Doubles.tryParse(str), Double.valueOf(defaults))).doubleValue();
    }

    public static short convertShort(String str, short defaults) {
        return str == null ? defaults : (short) convertInt(str, defaults);
    }

    public static float convertFloat(String str, float defaults) {
        return str == null ? defaults
                : ((Float) MoreObjects.firstNonNull(Floats.tryParse(str), Float.valueOf(defaults))).floatValue();
    }

    public static boolean convertBoolean(String str) {
        return Boolean.parseBoolean(str);
    }

    public static String[] split(String line, String seperator) {
        if (line != null && seperator != null && seperator.length() != 0) {
            ArrayList<String> list = new ArrayList();
            int pos1 = 0;

            while (true) {
                int pos2 = line.indexOf(seperator, pos1);
                if (pos2 < 0) {
                    list.add(line.substring(pos1));

                    for (int i = list.size() - 1; i >= 0 && ((String) list.get(i)).length() == 0; --i) {
                        list.remove(i);
                    }

                    return (String[]) list.toArray(new String[0]);
                }

                list.add(line.substring(pos1, pos2));
                pos1 = pos2 + seperator.length();
            }
        } else {
            return new String[0];
        }
    }

    public static int[] splitInt(String line, String seperator, int def) {
        String[] ss = split(line, seperator);
        int[] r = new int[ss.length];

        for (int i = 0; i < r.length; ++i) {
            r[i] = convertInt(ss[i], def);
        }

        return r;
    }

    public static Integer[] splitInteger(String line, String seperator, int def) {
        String[] ss = split(line, seperator);
        Integer[] r = new Integer[ss.length];

        for (int i = 0; i < r.length; ++i) {
            r[i] = Integer.valueOf(convertInt(ss[i], def));
        }

        return r;
    }

    public static long[] splitLong(String line, String separator, long def) {
        String[] ss = split(line, separator);
        long[] r = new long[ss.length];

        for (int i = 0; i < r.length; ++i) {
            r[i] = convertLong(ss[i], def);
        }

        return r;
    }

    public static String join(String separator, Collection<?> s) {
        return s != null && s.size() != 0 ? Joiner.on(separator).join(s).toString() : "";
    }

    public static String join(String separator, String[] s) {
        return s != null && s.length != 0 ? joinArray(separator, (Object[]) s) : "";
    }

    public static String joinArray(String separator, Object[] s) {
        return s != null && s.length != 0 ? Joiner.on(separator).join(s).toString() : "";
    }

    public static String joinArray(String separator, int[] s) {
        return s != null && s.length != 0 ? Ints.join(separator, s) : "";
    }

    public static String joinArray(String separator, long[] s) {
        return s != null && s.length != 0 ? Longs.join(separator, s) : "";
    }

    public static String join(String separator, Object... s) {
        return s != null && s.length != 0 ? joinArray(separator, s) : "";
    }

    public static String replaceAll(String s, String src, String dest) {
        if (s != null && src != null && dest != null && src.length() != 0) {
            int pos = s.indexOf(src);
            if (pos < 0) {
                return s;
            } else {
                int capacity = dest.length() > src.length() ? s.length() * 2 : s.length();
                StringBuilder sb = new StringBuilder(capacity);

                int writen;
                for (writen = 0; pos >= 0; pos = s.indexOf(src, writen)) {
                    sb.append(s, writen, pos);
                    sb.append(dest);
                    writen = pos + src.length();
                }

                sb.append(s, writen, s.length());
                return sb.toString();
            }
        } else {
            return s;
        }
    }

    public static String replaceFirst(String s, String src, String dest) {
        if (s != null && src != null && dest != null && src.length() != 0) {
            int pos = s.indexOf(src);
            if (pos < 0) {
                return s;
            } else {
                StringBuilder sb = new StringBuilder(s.length() - src.length() + dest.length());
                sb.append(s, 0, pos);
                sb.append(dest);
                sb.append(s, pos + src.length(), s.length());
                return sb.toString();
            }
        } else {
            return s;
        }
    }

    public static boolean isEmpty(String s) {
        return s == null ? true : s.trim().isEmpty();
    }

    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }

    public static String trim(String s) {
        return s == null ? null : s.trim();
    }

    public static String removeAll(String s, String src) {
        return replaceAll(s, src, "");
    }

    public static String abbreviate(String src, int maxlen, String replacement) {
        if (src == null) {
            return "";
        } else {
            if (replacement == null) {
                replacement = "";
            }

            StringBuffer dest = new StringBuffer();

            try {
                maxlen -= computeDisplayLen(replacement);
                if (maxlen < 0) {
                    return src;
                } else {
                    int i;
                    for (i = 0; i < src.length() && maxlen > 0; ++i) {
                        char c = src.charAt(i);
                        if (c >= 0 && c <= 255) {
                            --maxlen;
                        } else {
                            maxlen -= 2;
                        }

                        if (maxlen >= 0) {
                            dest.append(c);
                        }
                    }

                    if (i < src.length() - 1) {
                        dest.append(replacement);
                    }

                    return dest.toString();
                }
            } catch (Throwable var6) {
                var6.printStackTrace();
                return src;
            }
        }
    }

    public static String abbreviate(String src, int maxlen) {
        return abbreviate(src, maxlen, "");
    }

    public static String toShort(String str, int maxLen, String replacement) {
        if (str == null) {
            return "";
        } else if (str.length() <= maxLen) {
            return str;
        } else {
            StringBuilder dest = new StringBuilder();
            double len = 0.0D;

            for (int i = 0; i < str.length(); ++i) {
                char c = str.charAt(i);
                if (c >= 0 && c <= 255) {
                    len += 0.5D;
                } else {
                    ++len;
                }

                if (len > (double) maxLen) {
                    return dest.toString() + replacement;
                }

                dest.append(c);
            }

            return dest.toString();
        }
    }

    public static String toShort(String str, int maxLen) {
        return toShort(str, maxLen, "...");
    }

    public static int computeDisplayLen(String s) {
        int len = 0;
        if (s == null) {
            return len;
        } else {
            for (int i = 0; i < s.length(); ++i) {
                char c = s.charAt(i);
                if (c >= 0 && c <= 255) {
                    ++len;
                } else {
                    len += 2;
                }
            }

            return len;
        }
    }

    public static byte[] getUTF8Bytes(String s) {
        return s != null && s.length() >= 0 ? s.getBytes(UTF_8) : null;
    }

    public static byte[] getGBKBytes(String s) {
        return s != null && s.length() >= 0 ? s.getBytes(GBK) : null;
    }

    public static String getUTF8String(byte[] b) {
        return b != null ? new String(b, UTF_8) : null;
    }

    public static String getGBKString(byte[] b) {
        return b != null ? new String(b, GBK) : null;
    }

    public static Pair<String, String> splitByIndex(String input, int seperatorIndex) {
        return seperatorIndex == 0 ? Pair.makePair("", input.substring(1))
                : (seperatorIndex == input.length() - 1 ? Pair.makePair(input.substring(0, seperatorIndex), "")
                : Pair.makePair(input.substring(0, seperatorIndex), input.substring(seperatorIndex + 1)));
    }


    public static byte[] hexStrToBytes(String hexStr) {
        if (hexStr == null || hexStr.trim().equals("")) {
            return new byte[0];
        }
        if (hexStr.startsWith("0x")) {
            hexStr = hexStr.substring(2);
        }

        byte[] bytes = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            String subStr = hexStr.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }

        return bytes;
    }

    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String ubytesToHexStr(int[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        char[] buf = new char[bytes.length * 2];
        int index = 0;
        for (int b : bytes) {
            buf[index++] = HEX_CHAR[b >>> 4 & 0xf];
            buf[index++] = HEX_CHAR[b & 0xf];
        }

        return "0x" + new String(buf);
    }

    public static String bytesToHexStr(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        char[] buf = new char[bytes.length * 2];
        int index = 0;
        for (byte b : bytes) {
            buf[index++] = HEX_CHAR[b >>> 4 & 0xf];
            buf[index++] = HEX_CHAR[b & 0xf];
        }
        return "0x" + new String(buf);
    }

    public static byte[] ubytesToBytes(int[] ubytes) {
        byte[] bs = new byte[ubytes.length * 2];
        int index = 0;
        for (int b : ubytes) {
            bs[index++] = (byte) (b >>> 4 & 0xf);
            bs[index++] = (byte) (b & 0xf);
        }
        return bs;
    }
}
