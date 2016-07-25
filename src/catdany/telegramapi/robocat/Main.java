package catdany.telegramapi.robocat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import catdany.telegramapi.robocat.logging.Log;
import catdany.telegramapi.robocat.utils.Params;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

public class Main {
	
	private static String TELEGRAM_BOT_TOKEN;
	
	public static void main(String[] args) {
		Log.i("Started");
		readBotSettings();
		
		Bot bot = new Bot(TELEGRAM_BOT_TOKEN);
		bot.request("sendMessage", new Params()
			.add("chat_id", "-100052431")
			.add("text", "123"));
	}
	
	private static void readBotSettings() {
		JsonParser parser = new JsonParser();
		try {
			FileReader reader = new FileReader(new File("bot_settings.txt"));
			JsonObject json = parser.parse(reader).getAsJsonObject();
			
			TELEGRAM_BOT_TOKEN = json.get("token").getAsString();
		} catch (FileNotFoundException t) {
			Log.e("bot_settings.txt does not exist.", t);
		} catch (JsonParseException t) {
			Log.e("Unable to parse json in bot_settings.txt", t);
		}
	}
}
