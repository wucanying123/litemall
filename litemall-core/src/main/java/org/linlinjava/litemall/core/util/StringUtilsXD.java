package org.linlinjava.litemall.core.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类, 继承org.apache.commons.lang3.StringUtils类
 */
public class StringUtilsXD extends StringUtils {

	private static Logger logger = LoggerFactory.getLogger(StringUtilsXD.class);

	/**
	 * 首字母小写
	 *
	 * @param str
	 * @return
	 */
	public static String lowerFirst(String str) {
		if (StringUtils.isBlank(str)) {
			return "";
		} else {
			return str.substring(0, 1).toLowerCase() + str.substring(1);
		}
	}



	/**
	 * 首字母大写
	 *
	 * @param str
	 * @return
	 */
	public static String upperFirst(String str) {
		if (StringUtils.isBlank(str)) {
			return "";
		} else {
			return str.substring(0, 1).toUpperCase() + str.substring(1);
		}
	}

	/**
	 * 替换掉HTML标签方法
	 */
	public static String replaceHtml(String html) {
		if (isBlank(html)) {
			return "";
		}
		String regEx = "<.+?>";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(html);
		String s = m.replaceAll("");
		return s;
	}

	/**
	 * 缩略字符串（不区分中英文字符）
	 *
	 * @param str
	 *            目标字符串
	 * @param length
	 *            截取长度
	 * @return
	 */
	public static String abbr(String str, int length) {
		if (str == null) {
			return "";
		}
		try {
			StringBuilder sb = new StringBuilder();
			int currentLength = 0;
			for (char c : replaceHtml(StringEscapeUtils.unescapeHtml4(str)).toCharArray()) {
				currentLength += String.valueOf(c).getBytes("GBK").length;
				if (currentLength <= length - 3) {
					sb.append(c);
				} else {
					sb.append("...");
					break;
				}
			}
			return sb.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 缩略字符串（替换html）
	 *
	 * @param str
	 *            目标字符串
	 * @param length
	 *            截取长度
	 * @return
	 */
	public static String rabbr(String str, int length) {
		return abbr(replaceHtml(str), length);
	}

	/**
	 * 转换为Double类型
	 */
	public static Double toDouble(Object val) {
		if (val == null) {
			return 0D;
		}
		try {
			return Double.valueOf(trim(val.toString()));
		} catch (Exception e) {
			return 0D;
		}
	}

	/**
	 * 转换为Float类型
	 */
	public static Float toFloat(Object val) {
		return toDouble(val).floatValue();
	}

	/**
	 * 转换为Long类型
	 */
	public static Long toLong(Object val) {
		return toDouble(val).longValue();
	}

	/**
	 * 转换为Integer类型
	 */
	public static Integer toInteger(Object val) {
		return toLong(val).intValue();
	}


	/**
	 * 获得用户远程地址
	 */
	public static String getRemoteAddr(HttpServletRequest request) {
		String remoteAddr = request.getHeader("X-Real-IP");
		if (isNotBlank(remoteAddr)) {
			remoteAddr = request.getHeader("X-Forwarded-For");
		} else if (isNotBlank(remoteAddr)) {
			remoteAddr = request.getHeader("Proxy-Client-IP");
		} else if (isNotBlank(remoteAddr)) {
			remoteAddr = request.getHeader("WL-Proxy-Client-IP");
		}
		return remoteAddr != null ? remoteAddr : request.getRemoteAddr();
	}

	/**
	 * 是否有效手机号码，可以更改参数以只检查特定运营商的号段
	 *
	 * @param mobile
	 *            号码
	 * @return 是否合法手机号码
	 */
	@SuppressWarnings("unused")
	public static boolean isValidMobile(String mobile) {
		/**
		 * 手机号码 移动：134[0-8], 135, 136, 137, 138, 139, 150, 151, 157, 158, 159,
		 * 182, 187, 188 联通：130, 131, 132, 152, 155, 156, 185, 186 电信：133, 1349,
		 * 153, 180, 189
		 */
		String MOBILE = "^1(3[0-9]|5[0-35-9]|8[025-9])\\d{8}$";
		/*
		 * 中国移动 134[0-8],135,136,137,138,139,150,151,157,158,159,182,187,188
		 */
		String CM = "^1(34[0-8]|(3[5-9]|5[017-9]|8[278])\\d)\\d{7}$";
		/*
		 * 中国联通 130,131,132,152,155,156,185,186
		 */
		String CU = "^1(3[0-2]|5[256]|8[56])\\d{8}$";
		/*
		 * 中国电信 133,1349,153,180,189
		 */
		String CT = "^1((33|53|8[09])[0-9]|349)\\d{7}$";
		/*
		 * 大陆地区固话及小灵通 区号：010,020,021,022,023,024,025,027,028,029 号码：七位或八位
		 */
		String PHS = "^0(10|2[0-5789]|\\d{3})\\d{7,8}$";
		Pattern pattern = Pattern.compile(MOBILE);
		Matcher matcher = pattern.matcher(mobile);
		return matcher.matches();
	}

	public static boolean isMobileNO(String mobiles) {
		String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 页面编码转换
	 *
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static String decode(String value) throws Exception {
		try {
			if (value == null) {
				return null;
			}
			return new String(value.getBytes("ISO-8859-1"), "utf-8");
			// 注: 这里的utf-8, 应视提交页面的编码而定
		} catch (Exception ex) {
			return value;
		}
	}

	/**
	 * 去掉字符串最后一个","字符，并返回'str1','str2',...格式
	 *
	 * @param value
	 * @return
	 */
	public static String removeLastString(String value) {
		String rtnStr = "";
		String[] strs;
		if (",".equals(value.substring(value.length() - 1))) {
			strs = value.substring(0, value.length() - 1).split(",");
		} else {
			strs = value.split(",");
		}
		for (int i = 0; i < strs.length; i++) {
			strs[i] = "'" + strs[i] + "'";
			rtnStr = rtnStr + strs[i] + ",";
		}
		rtnStr = rtnStr.substring(0, rtnStr.length() - 1);
		return rtnStr;
	}

	/**
	 * 将科学记数法的数字转换为字符串
	 *
	 * @param value
	 * @return
	 */
	public static String scienConvert2String(String value) {
		if (value.indexOf(".") > 0 && value.indexOf("E") > 0) {
			BigDecimal bd = new BigDecimal(value);
			return bd.toPlainString();
		} else {
			return value;
		}
	}

	/**
	 * 将形如000000的字符串变成00:00:00形式
	 */
	public static String getStringTime(String value) {
		String temp1 = value.substring(0, 2);
		String temp2 = value.substring(2, 4);
		String temp3 = value.substring(4, 6);
		return temp1 + ":" + temp2 + ":" + temp3;
	}

	/**
	 * 随机产生4位数字
	 *
	 * @return
	 */
	public static String getValidateCode() {
		int[] array = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		Random rand = new Random();
		for (int i = 10; i > 1; i--) {
			int index = rand.nextInt(i);
			int tmp = array[index];
			array[index] = array[i - 1];
			array[i - 1] = tmp;
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 4; i++) {
			sb.append(array[i]);
		}

		return sb.toString();
	}

	/**
	 * 随机产生4位数字
	 * @return
	 */
	public static String get6ValidateCode() {
		int[] array = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		Random rand = new Random();
		for (int i = 10; i > 1; i--) {
			int index = rand.nextInt(i);
			int tmp = array[index];
			array[index] = array[i - 1];
			array[i - 1] = tmp;
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 6; i++) {
			sb.append(array[i]);
		}
		return sb.toString();
	}

	/**
	 * 将json字符串加密为base64
	 *
	 * @param jsonStr
	 * @return
	 */
	public static String getBase64(String jsonStr) {
		byte[] b = null;
		String s = null;
		try {
			b = jsonStr.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (b != null) {
			s = new String(Base64.encodeBase64(b));
		}
		return s;
	}

	/**
	 * 将字符串数组转换成json数组
	 *
	 * @param array
	 * @return
	 * @throws
	 */
	public static String arrayToJSONArray(String[] array, String path, String id, String objName) {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObj = new JSONObject();
		for (int i = 0; i < array.length; i++) {
			jsonObj = new JSONObject();
			jsonObj.put(objName, path + id + "/" + array[i]);
			jsonArray.add(jsonObj);
		}
		return jsonArray.toString();
	}

	/**
	 * 将字符串数组转换成json数组
	 *
	 * @param array
	 * @return
	 * @throws
	 */
	public static String arrayToJSONArrayWithoutId(String[] array, String path, String objName) {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObj = new JSONObject();
		for (int i = 0; i < array.length; i++) {
			if (StringUtils.isBlank(array[i])) {
				continue;
			}
			jsonObj = new JSONObject();
			jsonObj.put(objName, path + array[i]);
			jsonArray.add(jsonObj);
		}
		return jsonArray.toString();
	}

	public static String arrayToJSONArrayWithThumbnail(String[] array, String path, String objName, String suffix) {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObj = new JSONObject();
		for (int i = 0; i < array.length; i++) {
			if (StringUtils.isBlank(array[i])) {
				continue;
			}
			jsonObj = new JSONObject();
			jsonObj.put(objName, path + array[i] + suffix);
			jsonArray.add(jsonObj);
		}
		return jsonArray.toString();
	}

	public static String arrayToJSONArrayWithoutIdForMorePath(String[] array, String firstpath, String endpath,
			String objName) {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObj = new JSONObject();
		for (int i = 0; i < array.length; i++) {
			jsonObj = new JSONObject();
			jsonObj.put(objName, firstpath + array[i] + endpath);
			jsonArray.add(jsonObj);
		}
		return jsonArray.toString();
	}

	/**
	 * 将字符串数组转换成json数组 逆向工程
	 *
	 * @param
	 * @return
	 * @throws
	 */
	public static String[] invertArrayToJSONArrayWithoutId(String jsonArrStr, String objName) {
		JSONArray jsonArray = JSONArray.fromObject(jsonArrStr);
		String[] arr = new String[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObj = JSONObject.fromObject(jsonArray.get(i));
			arr[i] = (String) jsonObj.get(objName);
		}
		return arr;
	}

	/**
	 *
	 * @Title: toBytes @Description: 将一个字符串转换成二进制数组 @param @param
	 * str @param @return @return byte[] 返回值类型 @throws
	 */
	public static byte[] getBytes(String str) {

		return str.getBytes();

	}

	public static String getCombinStr(String str, int num, int sum) {
		if (num > 0) {
			if (sum == 1) {
				return str;
			}
			return str + "「" + num + "/" + sum + "」";
		} else {
			return str;
		}
	}

	public static String string2Json(String s) {
		if (s.indexOf("'") != -1) {
			// 将单引号转义一下，因为JSON串中的字符串类型可以单引号引起来的
			s = s.replaceAll("'", "\\'");
		}
		if (s.indexOf("\"") != -1) {
			// 将双引号转义一下，因为JSON串中的字符串类型可以单引号引起来的
			s = s.replaceAll("\"", "\\\"");
		}

		if (s.indexOf("\r\n") != -1) {
			// 将回车换行转换一下，因为JSON串中字符串不能出现显式的回车换行
			s = s.replaceAll("\r\n", "\\u000d\\u000a");
		}
		if (s.indexOf("\n") != -1) {
			// 将换行转换一下，因为JSON串中字符串不能出现显式的换行
			s = s.replaceAll("\n", "\\u000a");
		}
		return s;
	}

	/**
	 * 去重的join方法
	 *
	 * @param array
	 * @param separator
	 * @param startIndex
	 * @param endIndex
	 * @return
	 */
	public static String joinArr(final Object[] array, String separator, final int startIndex, final int endIndex) {
		if (array == null) {
			return null;
		}
		if (separator == null) {
			separator = EMPTY;
		}

		// endIndex - startIndex > 0: Len = NofStrings *(len(firstString) +
		// len(separator))
		// (Assuming that all Strings are roughly equally long)
		final int noOfItems = endIndex - startIndex;
		if (noOfItems <= 0) {
			return EMPTY;
		}

		final StringBuilder buf = new StringBuilder(noOfItems * 16);

		for (int i = startIndex; i < endIndex; i++) {
			if (i > startIndex && array[i] != null) {
				buf.append(separator);
			}
			if (array[i] != null) {
				buf.append(array[i]);
			}
		}
		return buf.toString();
	}

	/**
	* @Title: isNotBlankParams
	* @Description: 判断多参数中是否有空
	* @param @param params
	* @param @return    参数
    * @return boolean    返回类型
	* @throws
	 */
	public static boolean hasBlankParams(String... params){
		for (int i = 0; i < params.length; i++) {
			if(isBlank(params[i])){
				return true;
			}
		}
		return false;
	}

	public static String formatMobileOrUsername(String mobile){
		if(StringUtils.isEmpty(mobile)){
			return null;
		}
		mobile = mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
		return mobile;
	}

	/**
	 * 中文2个字符,英文1个字符 向上取整
	 * @param value
	 * @return
	 */
	public static int string_length(String value) {
		value = replaceBlank(value);
		int valueLength = 0;
		String chinese = "[\u4e00-\u9fa5]";
		for (int i = 0; i < value.length(); i++) {
			String temp = value.substring(i, i + 1);
			if (temp.matches(chinese)) {
				valueLength += 2;
			} else {
				valueLength += 1;
			}
		}
		return (int)Math.ceil(valueLength/2.0);
	}


	public static String replaceBlank(String str) {
		String dest = "";
		if (str!=null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}


	public static BaseResp<Object> setBaseResp(BaseResp<Object> baseResp, String result){
		try {
			result = replaceBlank(result);
			JSONObject jsonObject = JSONObject.fromObject(result);
			baseResp.setData(jsonObject);
			if (null != jsonObject.get("_type")) {
				String _type = jsonObject.get("_type").toString();
				if (_type.equals("success")) {
					baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
				}
			}
		} catch (Exception e) {
			logger.error("setBaseResp error", e);
		}
		return baseResp;
	}

	public static int checkPageNumParam(Integer pageNumParam) {
		return pageNumParam != null && pageNumParam >= 1 ? pageNumParam : Constant.PAGE_INDEX_FIRST;
	}

	public static int checkPageSizeParam(Integer pageSizeParam) {
		return pageSizeParam != null && pageSizeParam > 0 ? pageSizeParam : Constant.PAGE_INDEX_SIZE;
	}

	public static String getFileMD5(File file) {
		StringBuffer sb = new StringBuffer("");
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");

			md.update(FileUtils.readFileToByteArray(file));
			byte b[] = md.digest();
			int d;
			for (int i = 0; i < b.length; i++) {
				d = b[i];
				if (d < 0) {
					d = b[i] & 0xff;
					// 与上一行效果等同
					// i += 256;
				}
				if (d < 16)
					sb.append("0");
				sb.append(Integer.toHexString(d));
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}
