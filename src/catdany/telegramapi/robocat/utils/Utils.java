package catdany.telegramapi.robocat.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import catdany.telegramapi.robocat.logging.Log;
import catdany.telegramapi.robocat.telegram.Message;
import catdany.telegramapi.robocat.telegram.User;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Utils {
	
	public static final String IP_API_URL = "http://api.ipify.org";
	
	public static String getIP() {
		try {
			URL url = new URL(IP_API_URL);
			InputStream ins = url.openStream();
			InputStreamReader inr = new InputStreamReader(ins);
			
			StringBuilder str = new StringBuilder();
			char[] buf = new char[16];
			int len;
			while ((len = inr.read(buf)) >= 0) {
				str.append(buf, 0, len);
			}
			
			return str.toString();
		} catch (IOException t) {
			Log.e("Unable to get IP address.", t);
			return null;
		}
	}
	
	public static String amount(int amount, String one, String few, String several) {
		if (amount % 10 == 1 && amount != 11)
			return amount + " " + one;
		else if (amount % 10 >= 2 && amount % 10 <= 4 && amount < 12 && amount > 14)
			return amount + " " + few;
		else
			return amount + " " + several;
	}
	
	public static String concatenateSubarray(String[] array, int start, String separator) {
		StringBuilder str = new StringBuilder();
		for (int i = start; i < array.length; i++) {
			str.append(separator + array[i]);
		}
		return str.substring(1);
	}
	
	public static <T>T draw(T[] pool) {
		return pool[(int)(Math.random() * pool.length)];
	}
	
	public static String string(JsonElement json) {
		return json != null ? json.getAsString() : null;
	}
	
	public static int integer(JsonElement json) {
		return json != null ? json.getAsInt() : 0;
	}
	
	public static long longint(JsonElement json) {
		return json != null ? json.getAsLong() : 0;
	}
	
	public static JsonObject jsonobj(JsonElement json) {
		return json != null ? json.getAsJsonObject() : null;
	}
	
	public static Message msg(JsonElement json) {
		return json != null ? new Message(json) : null;
	}
	
	public static User user(JsonElement json) {
		return json != null ? new User(json) : null;
	}
}
