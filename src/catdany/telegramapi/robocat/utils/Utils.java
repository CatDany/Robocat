package catdany.telegramapi.robocat.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;

import catdany.telegramapi.robocat.logging.Log;
import catdany.telegramapi.robocat.telegram.Message;
import catdany.telegramapi.robocat.telegram.Photo;
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
	
	public static InputStream getURLStream(URL url) throws IOException {
		URLConnection connection = url.openConnection();
		try {
		    return connection.getInputStream();
		} catch (IOException httpError) {
		    if (connection instanceof HttpURLConnection) {
		        HttpURLConnection httpConn = (HttpURLConnection)connection;
		        int statusCode;
				try {
					statusCode = httpConn.getResponseCode();
			        if (statusCode != 200) {
			            return httpConn.getErrorStream();
			        } else {
			        	throw httpError;
			        }
				} catch (IOException t) {
					throw new IOException("Unable to get response code from URLConnection.", t);
				}
		    } else {
		    	throw new ClassCastException("URLConnection is not an instance of HttpURLConnection.");
		    }
		}
	}
	
	public static String amount(int amount, String one, String few, String several) {
		if (amount % 10 == 1 && amount != 11)
			return String.format(one, amount);
		else if (amount % 10 >= 2 && amount % 10 <= 4 && (amount < 12 || amount > 14))
			return String.format(few, amount);
		else
			return String.format(several, amount);
	}
	
	public static String concatenateSubarray(String[] array, int start, String separator) {
		StringBuilder str = new StringBuilder();
		for (int i = start; i < array.length; i++) {
			str.append(separator + array[i]);
		}
		return str.substring(1);
	}
	
	public static int getCal(long ms, int field) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(ms);
		return cal.get(field);
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
	
	public static Photo photo(JsonElement json) {
		return json != null ? new Photo(json) : null;
	}
}
