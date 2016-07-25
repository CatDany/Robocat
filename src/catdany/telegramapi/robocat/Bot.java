package catdany.telegramapi.robocat;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import catdany.telegramapi.robocat.logging.Log;
import catdany.telegramapi.robocat.utils.Params;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Bot {
	
	public static final String TELEGRAM_REQUEST_URL = "https://api.telegram.org/bot";
	
	private String token;
	
	public Bot(String token) {
		this.token = token;
	}
	
	private String getRequestURL(String method, Params params) {
		return TELEGRAM_REQUEST_URL + token + "/" + method + "?" + params.toString();
	}
	
	public APIResponse request(String method, Params params) {
		String requestURL = getRequestURL(method, params);
		try {
			URL url = new URL(requestURL);
			InputStreamReader isr = new InputStreamReader(url.openStream());
			JsonParser parser = new JsonParser();
			JsonElement json = parser.parse(isr);
			return new APIResponse(json);
		} catch (IOException t) {
			Log.e("Unable to perform API request: " + requestURL, t);
			return new APIResponse(null);
		}
	}
}
