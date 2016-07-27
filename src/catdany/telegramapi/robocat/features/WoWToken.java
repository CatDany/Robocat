package catdany.telegramapi.robocat.features;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import catdany.telegramapi.robocat.logging.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class WoWToken {
	
	public static final String WOW_TOKEN_API = "https://wowtoken.info/wowtoken.json";
	
	public static int price;
	public static int min24;
	public static int max24;
	public static long timestamp;
	
	public static void update() {
		if (System.currentTimeMillis()/1000 - timestamp > 60*10) {
			Log.i("Fetching data WoWToken.info API...");
			forceUpdateTokenPrice();
		}
	}
	
	private static void forceUpdateTokenPrice() {
		try {
			URL url = new URL(WOW_TOKEN_API);
			InputStreamReader isr = new InputStreamReader(url.openStream());
			JsonParser parser = new JsonParser();
			JsonElement json = parser.parse(isr);
			isr.close();
			
			JsonObject eu = json.getAsJsonObject().get("EU").getAsJsonObject();
			JsonObject raw = eu.get("raw").getAsJsonObject();
			price = raw.get("buy").getAsInt();
			min24 = raw.get("24min").getAsInt();
			max24 = raw.get("24max").getAsInt();
			timestamp = eu.get("timestamp").getAsLong();
		} catch (IOException t) {
			Log.e("Unable to perform WoWToken.info API request.", t);
		}
	}
	
}