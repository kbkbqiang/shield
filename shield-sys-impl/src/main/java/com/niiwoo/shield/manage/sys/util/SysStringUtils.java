package com.niiwoo.shield.manage.sys.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


public class SysStringUtils {

	public static Random rd = new Random();
	private static final String SIGN_WHITE_SPACE=" ";

	/**
	 * 验证字符串是否包含在集合
	 * @param str
	 * @param collection
	 * @return
	 */
	public static boolean contain(String str, Collection<String> collection){
		return collection.contains(str);
	}


	/**
	 * 根据经纬度计算梁坐标的距离，单位：米
	 * @param lat1		纬度1 单位：度
	 * @param lon1		经度1 单位：度
	 * @param lat2		纬度2 单位：度
	 * @param lon2		经度2 单位：度
	 * @return	double 返回类型 单位：米
	 */
    public static double getDistance(double lat1, double lon1, double lat2, double lon2){  
	  double radLat1 = lat1 * Math.PI / 180;  
	  double radLat2 = lat2 * Math.PI / 180;  
	  double a = radLat1 - radLat2;  
	  double b = lon1 * Math.PI / 180 - lon2 * Math.PI / 180;  
	  double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)  
	          + Math.cos(radLat1) * Math.cos(radLat2)  
	          * Math.pow(Math.sin(b / 2), 2)));  
		s = s * 6378137.0;// 取WGS84标准参考椭球中的地球长半径(单位:m)
	  s = Math.round(s * 10000) / 10000;  
	  
	  double d = 6378137.0 * 2 * Math.asin(Math.sqrt(Math.pow(Math.sin((lat1-lat2)*Math.PI/180 / 2), 2)  
	          + Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180)  
	          * Math.pow(Math.sin((lon1-lon2) * Math.PI / 180 / 2), 2)));
	  return s;  
    }


	public static double getDistance(String lat1Str, String lon1Str, String lat2Str, String lon2Str){
        double lat1 = NumberUtils.toDouble(lat1Str,0);
		double lon1 = NumberUtils.toDouble(lon1Str,0);
		double lat2 = NumberUtils.toDouble(lat2Str,0);
		double lon2 = NumberUtils.toDouble(lon2Str,0);
		double radLat1 = lat1 * Math.PI / 180;
		double radLat2 = lat2 * Math.PI / 180;
		double a = radLat1 - radLat2;
		double b = lon1 * Math.PI / 180 - lon2 * Math.PI / 180;
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * 6378137.0;// 取WGS84标准参考椭球中的地球长半径(单位:m)
		s = Math.round(s * 10000) / 10000;

		double d = 6378137.0 * 2 * Math.asin(Math.sqrt(Math.pow(Math.sin((lat1-lat2)*Math.PI/180 / 2), 2)
				+ Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180)
				* Math.pow(Math.sin((lon1-lon2) * Math.PI / 180 / 2), 2)));
		return s;
	}

	/**
	 * 获取所有参数的最小值
	 * @param d
	 * @return
	 */
    public static double min(double...d){
    	double result = Double.MAX_VALUE;
    	for(double dd:d){
    		if(result>dd){
    			result=dd;
    		}
    	}
		return result;
    }

	/**
	 * 从一个集合复制数据到另一个集合
	 * @param c1
	 * @param c2
	 * @param begin
	 * @param end
	 */
    public static void copy(Collection c1, Collection c2,int begin,int end){
    	if(c2.isEmpty()){
    		return ;
    	}
    	Iterator it = c2.iterator();
    	int count = 0;
    	while(it.hasNext()){
    		Object o = it.next();
    		if(count>=begin&& count<=end){
    			c1.add(o);
    		}
    		count ++;
    	}
    }

	/**
	 * 从一个集合复制数据到另一个集合
	 * @param c1
	 * @param c2
	 */
    public static void copy(Collection c1, Collection c2){
    	copy(c1, c2, 0, c2.size());
    }


	/**
	 * MD5加密
	 * @param plainText		需要加密的对象
	 * @return	String 返回类型 返回加密后的MD5值
	 */
	public static String getMD5(String plainText) {
		if (plainText == null || "".equals(plainText)) {
			throw new RuntimeException("加密对象为空");
		}
		byte[] secretBytes = null;
		try {
			secretBytes = MessageDigest.getInstance("md5").digest(
					plainText.getBytes());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("没有md5这个算法！");
		}
		String md5code = new BigInteger(1, secretBytes).toString(16);// 16进制数字
		// 如果生成数字未满32位，需要前面补0
		for (int i = 0; i < 32 - md5code.length(); i++) {
			md5code = "0" + md5code;
		}
		return md5code;
	}


	/**
	 * 验证是否是手机号码
	 * @param str
	 * @return
	 */
	public static boolean isPhoneNum(String str){
		if(StringUtils.isEmpty(str)){
			return false;
		}
		return str.matches("^((\\+86)|(86))?[1]\\d{10}$");
	}

	/**
	 * 验证是否是手机号码
	 * @param str
	 * @return
	 */
	public static boolean isPhoneNumLike(String str){
		if(StringUtils.isEmpty(str)){
			return false;
		}
		return str.matches("^((\\+86)|(86))?[1]\\d{5,10}$");
	}


	/**
	 * 断是否为正确的邮件格式
	 * @param str
	 * @return
	 */
	public static boolean isEmail(String str){
        if(StringUtils.isEmpty(str)) {
			return false;
		}  
        return str.matches("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$");  
    }

	/**
	 * 判断字符串是否为数字
	 * @param str
	 * @return
	 */
	private static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");  
        Matcher isNum = pattern.matcher(str);  
        if (isNum.matches()) {  
            return true;  
        } else {  
            return false;  
        }  
    }


	/**
	 * 判断字符串是否为日期格式
	 * @param strDate
	 * @return
	 */
	public static boolean isDate(String strDate) {
        Pattern pattern = Pattern  
                .compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");  
        Matcher m = pattern.matcher(strDate);  
        if (m.matches()) {  
            return true;  
        } else {  
            return false;  
        }  
    }  
    
    public static String toJavaVarriableName(String name){
    	if(name == null || name.length() == 0) {
			return null;
		}
    	char[] chars = name.toCharArray();
		int pos = -1;
		char c;
		for (int i = 0; i < chars.length; i++) {
			c = chars[i];
			if(c == '_'){
				pos = i;
			}else if (c >= 'a' && c <= 'z' && pos != -1 && pos == i - 1) {
				chars[i] = (char) (c - 32);
			}
		}
		return new String(chars).replaceAll("_", "");
    }
    
    public static String toUnderlineVarribleName(String name){
    	if(name == null || name.length() == 0) {
			return null;
		}
    	char[] chars = name.toCharArray();
		char c;
		StringBuffer sb  = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			c = chars[i];
			if(i > 0 && c >= 'A' && c <= 'Z'){
				sb.append('_');
				sb.append((char) (c + 32));
			}else{
				sb.append(c);
			}
		}
		return sb.toString();
    }


	/**
	 * 用于真实姓名关键信息替换
	 * @param realName	真实姓名
	 * @param start		起始值
	 * @return
	 */
	public static String encryptionRealName(String realName, int start) {
		if (StringUtils.isEmpty(realName)) {
			return "";
		}
		int length = realName.length();
		String newString = "";
		for (int i = 0; i < length - start; i++) {
			newString += "*";
		}
		return realName.substring(0, start) + newString;
	}


	/**
	 * 通过手机号码生成邀请码
	 * @param mobileNo	手机号码
	 * @return	String 返回类型 邀请码
	 */
	public static String getInvitationCodeByMobileNo(String mobileNo) {
		if(StringUtils.isEmpty(mobileNo) || !isPhoneNum(mobileNo)) {
			return "";
		}
		// 二进制
		String binaryChar = Long.toBinaryString(Long.valueOf(mobileNo.substring(1)));
		char arrays[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J',
				'K', 'M', 'N', 'P', 'Q', 'R', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
		StringBuffer tempZero = new StringBuffer("");
		for (int i = 0; i < 35 - binaryChar.length(); i++) {
			tempZero = tempZero.append("0");
		}
		binaryChar = tempZero.toString().concat(binaryChar);
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < 7; i++) {
			result = result.append(arrays[Integer.valueOf(binaryChar.substring(i * 5, i * 5 + 5),
					2)]);
		}
		return result.toString();
	}
	
	/**
	 * 过滤特殊字符， 只允许字母和数字和中文//[\\pP‘’“” 以及包含中间空格
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
        	if (p.matches(regEx, String.valueOf(c))) {  
                sb.append(c);  
            }  
        }  
        return sb.toString();  
    }


	/**
	 * 字符串转换为Integer
	 * @param obj 字符串
	 * @param def 默认值
	 * @return Integer, 字符串为null时返回def
	 */
	public static Integer getInteger(Object obj, int def) {
		String str = obj == null ? "" : obj.toString();
		Integer i = null;

		if (str.trim().length() == 0) {
			i = new Integer(def);
		} else {
			try {
				i = Integer.valueOf(str);
			} catch (Exception e) {
			}
		}

		return i == null ? new Integer(def) : i;
	}


	/**
	 * 判断当前对象是否为空，“null”、null、“”
	 * @param obj
	 * @return
     */
	public static boolean isEmpty(final Object obj) {
		if (null == obj || "".equals(obj) || "null".equals(obj)) {
			return true;
		}
		return  false;
	}


	/**
	 * Map 对象取值转为为String 类型
	 * @param obj Map 对象中对应具体的value 值
	 * @param defaultValue 返回默认值
	 * @return
     */
	public static String toString(final Object obj, final String defaultValue) {
		if (isEmpty(obj)) {
			return defaultValue;
		}
		return obj.toString();
	}


	/**
	 * Map 对象取值转为为String 类型
	 * @param obj Map 对象中对应具体的value 值
	 * @return
	 */
	public static String toString(final Object obj) {
		if (isEmpty(obj)) {
			return "";
		}
		return obj.toString();
	}


	/**
	 * 是否以空白字符开头
	 *
	 * @param paramStr
	 * @return
	 * @author TomXue
	 */
	public static boolean isStartWithWhiteSpace(String paramStr) {
		if (StringUtils.isNotEmpty(paramStr)) {
			return StringUtils.startsWith(paramStr, SIGN_WHITE_SPACE);
		}
		return false;
	}


	/**
	 * 判断长度是否在范围内
	 *
	 * @param paramStr
	 * @param length
	 * @return
	 * @author TomXue
	 */
	public static boolean isWithinLength(String paramStr, int length) {
		if (StringUtils.isNotBlank(paramStr)) {
			return paramStr.length() <= length;
		}

		return false;
	}

	/**
	 * 判断长度是否在范围内(区分中英文)
	 * @param paramStr
	 * @param minLength
	 * @param maxLength
	 * @return
	 */
	public static boolean isWithinLengthOfDistinguishChinese(String paramStr, int minLength, int maxLength) {
		if (StringUtils.isNotBlank(paramStr)) {
			return minLength <= length(paramStr) && length(paramStr) <= maxLength;
		}
		return false;
	}


	/**
	 * 区分中英文获取长度
	 * @param value
	 * @return
	 * @author TomXue
	 */
	public static int length(String value) {
		int valueLength = 0;
		String chinese = "[\u0391-\uFFE5]";
		/* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
		for (int i = 0; i < value.length(); i++) {
			/* 获取一个字符 */
			String temp = value.substring(i, i + 1);
			/* 判断是否为中文字符 */
			if (temp.matches(chinese)) {
				/* 中文字符长度为2 */
				valueLength += 2;
			} else {
				/* 其他字符长度为1 */
				valueLength += 1;
			}
		}
		return valueLength;
	}

	/**
	 * 生成手机验证码
	 * @return
	 */
	public static String getMsgCode(){
		return 100000 + rd.nextInt(899999)+"";
	}


	/**
	 * 生成短信验证码
	 * @param codeLen	要生成验证码的长度
	 * @param metadata	验证字符需要的元数据
	 * @return
	 */
	public static String getSmsCode(int codeLen, char[] metadata){
		if (metadata==null){
			metadata = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
		}
		StringBuilder stringBuilder = new StringBuilder();
		Random random = new Random();
		for (int i=0; i< codeLen; i++){
			int index = random.nextInt(metadata.length);
			stringBuilder.append(metadata[index]);
		}
		return stringBuilder.toString();
	}

	/**
	 * 生成短信验证码
	 * @param codeLen 验证码长度
	 * @return
	 */
	public static String getSmsCode(int codeLen){
		return getSmsCode(codeLen, null);
	}


	/**
	 * 主函数
	 * @param args
     */
	public static void main(String[] args) {
//		System.out.println("distance: "+SysStringUtils.getDistance(22.543099, 114.057868,
//				22.5602108834784,113.9570786552398) );

		System.out.println(getSmsCode(6,null));

		System.out.println(getMsgCode());
	}
}
