package catdany.telegramapi.robocat.features;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

import catdany.telegramapi.robocat.Main;
import catdany.telegramapi.robocat.logging.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class BattleNetAPI {
	
	public static final String ARMORY_API_REQUEST_LINK = "https://eu.api.battle.net/wow/character/%s/%s?locale=ru_RU&apikey=%s";
	public static final String ARMORY_LINK = "http://eu.battle.net/wow/ru/character/%s/%s/simple";
	public static final String ARMORY_THUMBNAIL = "https://render-api-eu.worldofwarcraft.com/static-render/eu/";
	
	public static String getArmoryRequestURL(String realm, String character) {
		try {
			return String.format(ARMORY_API_REQUEST_LINK,
					URLEncoder.encode(realm, "UTF-8"),
					URLEncoder.encode(character, "UTF-8"),
					Main.BATTLENET_APP_TOKEN);
		} catch (UnsupportedEncodingException t) {
			Log.e("UTF-8 is unsupported.", t);
			return "http://utf8isnotsupportedkillyourself.com";
		}
	}
	
	public static ArmoryData requestArmoryData(String realm, String character) {
		try {
			URL url = new URL(getArmoryRequestURL(realm, character));
			InputStreamReader isr = new InputStreamReader(url.openStream());
			JsonParser parser = new JsonParser();
			JsonElement json = parser.parse(isr);
			isr.close();
			
			if (json.getAsJsonObject().has("status") && json.getAsJsonObject().get("status").getAsString().equals("nok")) {
				return null;
			}
			return new ArmoryData(json);
		} catch (IOException t) {
			Log.e("Unable to request armory data.", t);
			return null;
		}
	}
	
}
