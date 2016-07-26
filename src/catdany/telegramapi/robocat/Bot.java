package catdany.telegramapi.robocat;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import catdany.telegramapi.robocat.logging.Log;
import catdany.telegramapi.robocat.telegram.Update;
import catdany.telegramapi.robocat.utils.Params;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Bot {
	
	public static final String TELEGRAM_REQUEST_URL = "https://api.telegram.org/bot";
	
	private String token;
	
	private int lastUpdateId = 0;
	
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
			isr.close();
			return new APIResponse(json);
		} catch (IOException t) {
			Log.e("Unable to perform API request: " + requestURL, t);
			return new APIResponse(null);
		}
	}
	
	public Update[] getUpdates() {
		APIResponse r = request("getUpdates", new Params().add("offset", "" + (lastUpdateId+1)));
		JsonArray result = r.getResultAsArray();
		Update[] updates = new Update[result.size()];
		for (int i = 0; i < result.size(); i++) {
			updates[i] = new Update(result.get(i));
			if (updates[i].getId() > lastUpdateId) {
				lastUpdateId = updates[i].getId();
			}
		}
		return updates;
	}
	
	public APIResponse sendMessage(String chatId, String message, String parseMode, boolean disableWebPagePreview) {
		return request("sendMessage", new Params()
				.add("chat_id", chatId)
				.add("text", message)
				.add("parse_mode", parseMode)
				.add("disable_web_page_preview", "" + disableWebPagePreview));
	}
	
	public APIResponse sendMessage(String chatId, String message) {
		return sendMessage(chatId, message, "", false);
	}
}
