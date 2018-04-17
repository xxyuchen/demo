package com.geeker.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;


public final class WebUtils {

	/**
	 * 不可实例化
	 */
	private WebUtils() {
	}

	/**
	 * 获取参数
	 * 
	 * @param queryString
	 *            查询字符串
	 * @param encoding
	 *            编码格式
	 * @param name
	 *            参数名称
	 * @return 参数
	 */
	public static String getParameter(String queryString, String encoding, String name) {
		String[] parameterValues = getParameterMap(queryString, encoding).get(name);
		return parameterValues != null && parameterValues.length > 0 ? parameterValues[0] : null;
	}

	/**
	 * 获取参数
	 * 
	 * @param queryString
	 *            查询字符串
	 * @param encoding
	 *            编码格式
	 * @param name
	 *            参数名称
	 * @return 参数
	 */
	public static String[] getParameterValues(String queryString, String encoding, String name) {
		return getParameterMap(queryString, encoding).get(name);
	}

	/**
	 * 获取参数
	 * 
	 * @param queryString
	 *            查询字符串
	 * @param encoding
	 *            编码格式
	 * @return 参数
	 */
	public static Map<String, String[]> getParameterMap(String queryString, String encoding) {
		Map<String, String[]> parameterMap = new HashMap<String, String[]>();
		Charset charset = Charset.forName(encoding);
		if (StringUtils.isNotEmpty(queryString)) {
			byte[] bytes = queryString.getBytes(charset);
			if (bytes != null && bytes.length > 0) {
				int ix = 0;
				int ox = 0;
				String key = null;
				String value = null;
				while (ix < bytes.length) {
					byte c = bytes[ix++];
					switch ((char) c) {
					case '&':
						value = new String(bytes, 0, ox, charset);
						if (key != null) {
							putMapEntry(parameterMap, key, value);
							key = null;
						}
						ox = 0;
						break;
					case '=':
						if (key == null) {
							key = new String(bytes, 0, ox, charset);
							ox = 0;
						} else {
							bytes[ox++] = c;
						}
						break;
					case '+':
						bytes[ox++] = (byte) ' ';
						break;
					case '%':
						bytes[ox++] = (byte) ((convertHexDigit(bytes[ix++]) << 4) + convertHexDigit(bytes[ix++]));
						break;
					default:
						bytes[ox++] = c;
					}
				}
				if (key != null) {
					value = new String(bytes, 0, ox, charset);
					putMapEntry(parameterMap, key, value);
				}
			}
		}
		return parameterMap;
	}

	private static void putMapEntry(Map<String, String[]> map, String name, String value) {
		String[] newValues = null;
		String[] oldValues = map.get(name);
		if (oldValues == null) {
			newValues = new String[] { value };
		} else {
			newValues = new String[oldValues.length + 1];
			System.arraycopy(oldValues, 0, newValues, 0, oldValues.length);
			newValues[oldValues.length] = value;
		}
		map.put(name, newValues);
	}

	private static byte convertHexDigit(byte b) {
		if ((b >= '0') && (b <= '9')) {
			return (byte) (b - '0');
		}
		if ((b >= 'a') && (b <= 'f')) {
			return (byte) (b - 'a' + 10);
		}
		if ((b >= 'A') && (b <= 'F')) {
			return (byte) (b - 'A' + 10);
		}
		throw new IllegalArgumentException();
	}

	/**
	 * 获取IP地址
	 * 
	 * @return
	 */
	public static String getIp() {
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		String ip = null;
		if (requestAttributes != null) {
			HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
			ip = getIpAddr(request);
		}

		return ip;
	}

	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip.indexOf(",") > 0)
			ip = ip.split(",")[0];
		return ip;
	}

}
