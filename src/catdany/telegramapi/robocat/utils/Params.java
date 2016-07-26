package catdany.telegramapi.robocat.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map.Entry;

import catdany.telegramapi.robocat.logging.Log;

public class Params {
	
	private HashMap<String, String> map = new HashMap<String, String>();
	
	public Params add(String param, String value) {
		map.put(param, value);
		return this;
	}
	
	public Params remove(String param) {
		map.remove(param);
		return this;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (Entry<String, String> i : map.entrySet()) {
			try {
				str.append("&" + i.getKey() + "=" + URLEncoder.encode(i.getValue(), "UTF-8"));
			} catch (UnsupportedEncodingException t) {
				Log.e("UTF-8 is unsupported.",t);
				return null;
			}
		}
		return str.length() > 0 ? str.substring(1) : "";
	}
	
}
