package catdany.telegramapi.robocat.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import catdany.telegramapi.robocat.logging.Log;


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
}
