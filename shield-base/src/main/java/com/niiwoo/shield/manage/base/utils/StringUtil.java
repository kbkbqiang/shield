package com.niiwoo.shield.manage.base.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class StringUtil {

    public static String genRandomStr(int length) {
        char[] charPool = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
                'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
                'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        int count = charPool.length;
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(count);
            builder.append(charPool[index]);
        }
        return builder.toString();
    }

    /**
     * 计算两个字符串的编辑距离
     */
    public static int calculateEditDistance(String str1, String str2) {
        //        if (StringUtils.isEmpty(str1) && StringUtils.isEmpty(str2)) {
        //            return 0;
        //        }
        //        if (StringUtils.isEmpty(str1) && StringUtils.isNotEmpty(str2)) {
        //            return str2.length();
        //        }
        //        if (StringUtils.isNotEmpty(str1) && StringUtils.isEmpty(str2)) {
        //            return str1.length();
        //        }
        //计算两个字符串的长度。
        int len1 = str1.length();
        int len2 = str2.length();
        //建立上面说的数组，比字符长度大一个空间
        int[][] dif = new int[len1 + 1][len2 + 1];
        //赋初值，步骤B。
        for (int i = 0; i <= len1; i++) {
            dif[i][0] = i;
        }
        for (int i = 0; i <= len2; i++) {
            dif[0][i] = i;
        }
        //计算两个字符是否一样，计算左上的值
        int temp;
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    temp = 0;
                } else {
                    temp = 1;
                }
                //取三个值中最小的
                dif[i][j] = min(dif[i - 1][j - 1] + temp, dif[i][j - 1] + 1, dif[i - 1][j] + 1);
            }
        }
        //取数组右下角的值，同样不同位置代表不同字符串的比较
        return dif[len1][len2];
    }

    /**
     * 两个字符串相似度
     *
     * @param str1
     * @param str2
     * @return
     */
    public static double calculateSimilar(String str1, String str2) {
        if (str1 == str2) return 1;
        int distance = calculateEditDistance(str1, str2);
        return 1 - (double) distance / Math.max(str1.length(), str2.length());
    }

    //得到最小值
    private static int min(int... is) {
        int min = Integer.MAX_VALUE;
        for (int i : is) {
            if (min > i) {
                min = i;
            }
        }
        return min;
    }

    public static String lpadding(int length, int number) {
        String format = "%0" + length + "d";
        return String.format(format, number);
    }

    public static String lpadding(int length, long input) {
        String format = "%0" + length + "d";
        return String.format(format, input);
    }

    /**
     * 验证码手机格式
     *
     * @param phoneNum
     * @return
     */
    public static boolean validatePhoneNum(String phoneNum) {
        if (StringUtils.isEmpty(phoneNum)) {
            return false;
        }
        return phoneNum.matches("^((\\+86)|(86))?[1]\\d{10}$");
    }

    /**
     * 密码格式验证
     *
     * @param password
     * @return
     */
    public static boolean validatePassword(String password) {
        if (StringUtils.isEmpty(password)) {
            return false;
        }
        String regex = "^(?=.*\\d.*)(?=.*[a-zA-Z].*).{6,16}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(password);
        return m.matches();
    }

    /**
     * 昵称格式验证
     *
     * @param nickName
     * @return
     */
    public static boolean validateNickName(String nickName) {
        if (StringUtils.isEmpty(nickName)) {
            return false;
        }
        String regex = "[`<>\\-~!#$%^&*()+=|{}':;\\\\'\"]";
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(nickName);
        return m.find();
    }

    /**
     * 生成UUID
     *
     * @return
     */
    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }

    /**
     * 纬度校验
     * 纬度： -90.0～+90.0（整数部分为0～90，必须输入1到5位小数）
     *
     * @param latitude
     * @return
     */
    public static boolean validateLatitude(String latitude) {
        if (StringUtils.isEmpty(latitude)) {
            return false;
        }

        String regex = "/^[\\-\\+]?([0-8]?\\d{1}\\.\\d{1,5}|90\\.0{1,5})$/";
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(latitude);

        return m.find();
    }

    /**
     * 经度校验
     * 经度： -180.0～+180.0（整数部分为0～180，必须输入1到5位小数）
     *
     * @param longitude
     * @return
     */
    public static boolean validateLongitude(String longitude) {
        if (StringUtils.isEmpty(longitude)) {
            return false;
        }

        String regex = "/^[\\-\\+]?(0?\\d{1,2}\\.\\d{1,5}|1[0-7]?\\d{1}\\.\\d{1,5}|180\\.0{1,5})$/";
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(longitude);

        return m.find();
    }


    /**
     * 过滤特殊字符， 只允许字母和数字和中文//[\\pP‘’“” 以及包含中间空格
     *
     * @param str 传入参数
     * @return 过滤后的字符串
     * @throws PatternSyntaxException 正则表达式错误
     */
    public static String stringFilter(final String str) throws PatternSyntaxException {
        // 只允许字母和数字和中文//[\\pP‘’“” 以及包含中间空格
        if (StringUtils.isEmpty(str) || StringUtils.isEmpty(str.trim())) {
            return str;
        }
        String strAfterTrim = str.trim();
        String regEx = "^[A-Za-z\\s+\\d\\u4E00-\\u9FA5\\p{P}‘’“”]+$";
        Pattern p = Pattern.compile(regEx);
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < strAfterTrim.length(); i++) {
            char c = strAfterTrim.charAt(i);
            if (Pattern.matches(regEx, String.valueOf(c))) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 判断IP地址的合法性，这里采用了正则表达式的方法来判断
     * return true，合法
     */
    public static boolean ipCheck(String text) {
        if (text != null && !text.isEmpty()) {
            // 定义正则表达式
            String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\." +
                    "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." +
                    "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." +
                    "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
            // 判断ip地址是否与正则表达式匹配
            // 返回判断信息
            // 返回判断信息
            return text.matches(regex);
        }
        return false;
    }

    /**
     * 将16进制字符串转为转换成字符串
     *
     * @param source
     * @return
     */
    public static byte[] hex2Bytes(String source) {
        byte[] sourceBytes = new byte[source.length() / 2];
        for (int i = 0; i < sourceBytes.length; i++) {
            sourceBytes[i] = (byte) Integer.parseInt(source.substring(i * 2, i * 2 + 2), 16);
        }
        return sourceBytes;
    }

    /**
     * 将byte[] 转换成字符串
     *
     * @return
     */
    public static String byte2Hex(byte[] srcBytes) {
        StringBuilder hexRetSB = new StringBuilder();
        for (byte b : srcBytes) {
            String hexString = Integer.toHexString(0x00ff & b);
            hexRetSB.append(hexString.length() == 1 ? 0 : "").append(hexString);
        }
        return hexRetSB.toString();
    }

    /**
     * 取消null，返回object.toString()或""
     *
     * @param object
     * @return
     */
    public static String toStringForNoNull(Object object) {
        String returnStr = "";
        if (null != object) {
            returnStr = object.toString();
        }
        return returnStr;
    }

    /**
     * 获取称呼
     *
     * @param sex
     * @param userName
     * @return
     */
    public static String getCallName(Byte sex, String userName) {
        String callName = "亲";
        if (null != sex && sex.doubleValue() == 1) { //性别：1-男，2-女
            if (null != userName) {
                callName = userName.substring(0, 1) + "先生";
            }
        } else if (null != sex && sex.doubleValue() == 2) {
            if (null != userName) {
                callName = userName.substring(0, 1) + "女士";
            }
        }
        return callName;
    }

    /**
     * 获取后4位字符串
     *
     * @param str
     * @return
     */
    public static String getFourLengthString(String str) {
        String returnStr = null;
        if (null != str && str.length() > 4) {
            returnStr = str.substring(str.length() - 4, str.length());
        } else {
            returnStr = str;
        }
        return returnStr;
    }

    /**
     * 获取后4位字符串
     *
     * @param str
     * @return
     */
    public static String getProjectIdString(String str) {
        String returnStr = null;
        if (null != str && str.length() > 5) {
            returnStr = "***" + str.substring(str.length() - 5, str.length());
        } else {
            returnStr = str;
        }
        return returnStr;
    }
}
