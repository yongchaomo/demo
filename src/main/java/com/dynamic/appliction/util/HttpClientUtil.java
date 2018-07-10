package com.dynamic.appliction.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class HttpClientUtil {
	private HttpClientUtil() {
	}

	public static String getGoogleAccessToken(String code) {
		return null;
	}

	public static String[] getStringByPost(String token_url, HashMap<String, String> params, Object object) {
		return null;

	}

	public static String[] sendGetRequest(String token_url, HashMap<String, String> params) {
		return null;
	}

	public static String[] getStringByGet(String token_url, Map<String, String> params) {
		return null;
	}

	public static String getUrl(String Url, Map<String, String> params) {
		if (params != null) {
			Iterator<String> it = params.keySet().iterator();
			StringBuffer sb = null;
			while (it.hasNext()) {
				String key = it.next();
				String value = params.get(key);
				if (sb == null) {
					sb = new StringBuffer();
					sb.append("?");
				} else {
					sb.append("&");
				}
				sb.append(key);
				sb.append("=");
				sb.append(value);
			}
			Url += sb.toString();
		}
		return Url;

	}

}
