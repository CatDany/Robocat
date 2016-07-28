package catdany.telegramapi.robocat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import catdany.telegramapi.robocat.features.Pamphlets;
import catdany.telegramapi.robocat.logging.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

public class Main {
	
	public static final String VERSION_COMMIT_HASH = "fb8448516871c6d41decbd3282bd1815c87e591f";
	
	public static String TELEGRAM_BOT_TOKEN;
	public static String BATTLENET_APP_TOKEN;
	public static String HELP_COMMAND_TEXT;
	public static long BOT_UPDATE_REQUEST_DELAY;
	
	public static final String AMARI_STICKERS[] = new String[] {
		"BQADAgADKwADqxirAuIwV_5H4vi9Ag", "BQADAgADLQADqxirAhK0IQQETh_aAg", "BQADAgADMAADqxirAgozQ6vpv8cTAg", "BQADAgADNQADqxirAlx663SXJiPkAg", "BQADAgADNwADqxirAmVdGcgE-kWNAg"
	};
	public static final String OW_STICKERS[] = new String[] {
		"BQADAQADAwADJBlPCz-1SHLhWOYbAg","BQADAQADBQADJBlPC4oOLNN4paabAg","BQADAQADBwADJBlPC_aTzmzu_-O6Ag","BQADAQADCQADJBlPC4SHvtfhm8nUAg","BQADAQADCwADJBlPC8LEzIZzv0ajAg","BQADAQADDQADJBlPC1f6ZfwPsDpfAg","BQADAQADDwADJBlPC50DdIn9Om2vAg","BQADAQADEQADJBlPCyO6duepWPLvAg","BQADAQADEwADJBlPC-zFg7FaoR5DAg","BQADAQADFQADJBlPC6_W0r3Qu3JjAg","BQADAQADFwADJBlPC-l4j5sQZ92FAg","BQADAQADGQADJBlPC3TCN6X3L2QNAg","BQADAQADGwADJBlPCwXZKcrRRCXqAg","BQADAQADHQADJBlPC3VtPxEO2id8Ag","BQADAQADHwADJBlPC_YZHY0Wl2kwAg","BQADAQADIQADJBlPC6QTBNilJkBSAg","BQADAQADIwADJBlPC33Te7Pqi42aAg","BQADAQADJQADJBlPC81ZR2VWV9dFAg","BQADAQADJwADJBlPC4dpb_CkeBVGAg","BQADAQADKQADJBlPCxDoL8sfQ1MKAg"
	};
	
	public static final int LEGION_LAUNCH_TIME = 1472594400;
	
	public static void main(String[] args) {
		Log.init();
		Log.i("Started Robocat. Build (Commit Hash): " + VERSION_COMMIT_HASH);
		readBotSettings();
		Pamphlets.load();
		
		Bot bot = new Bot(TELEGRAM_BOT_TOKEN);
		BotHandler botHandler = new BotHandler(bot, BOT_UPDATE_REQUEST_DELAY);
		BotCommands.init(botHandler);
		botHandler.start();
	}
	
	private static void readBotSettings() {
		JsonParser parser = new JsonParser();
		try {
			FileReader reader = new FileReader(new File("bot_settings.txt"));
			JsonObject json = parser.parse(reader).getAsJsonObject();
			reader.close();
			
			TELEGRAM_BOT_TOKEN = json.get("telegram_token").getAsString();
			BATTLENET_APP_TOKEN = json.get("battlenet_token").getAsString();
			HELP_COMMAND_TEXT = json.get("help").getAsString();
			BOT_UPDATE_REQUEST_DELAY = json.get("update_request_delay").getAsLong();
		} catch (FileNotFoundException t) {
			Log.e("bot_settings.txt does not exist.", t);
		} catch (JsonParseException | IOException t) {
			Log.e("Unable to parse json in bot_settings.txt", t);
		}
	}
}
