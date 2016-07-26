package catdany.telegramapi.robocat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import catdany.telegramapi.robocat.logging.Log;
import catdany.telegramapi.robocat.telegram.Message;
import catdany.telegramapi.robocat.utils.Params;
import catdany.telegramapi.robocat.utils.Utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

public class Main {
	
	public static final String VERSION_COMMIT_HASH = "";
	
	private static String TELEGRAM_BOT_TOKEN;
	private static String HELP_COMMAND_TEXT;
	public static long BOT_UPDATE_REQUEST_DELAY;
	
	public static final String AMARI_STICKERS[] = new String[] {
		"BQADAgADKwADqxirAuIwV_5H4vi9Ag", "BQADAgADLQADqxirAhK0IQQETh_aAg", "BQADAgADMAADqxirAgozQ6vpv8cTAg", "BQADAgADNQADqxirAlx663SXJiPkAg"
	};
	public static final String OW_STICKERS[] = new String[] {
		"BQADAQADAwADJBlPCz-1SHLhWOYbAg","BQADAQADBQADJBlPC4oOLNN4paabAg","BQADAQADBwADJBlPC_aTzmzu_-O6Ag","BQADAQADCQADJBlPC4SHvtfhm8nUAg","BQADAQADCwADJBlPC8LEzIZzv0ajAg","BQADAQADDQADJBlPC1f6ZfwPsDpfAg","BQADAQADDwADJBlPC50DdIn9Om2vAg","BQADAQADEQADJBlPCyO6duepWPLvAg","BQADAQADEwADJBlPC-zFg7FaoR5DAg","BQADAQADFQADJBlPC6_W0r3Qu3JjAg","BQADAQADFwADJBlPC-l4j5sQZ92FAg","BQADAQADGQADJBlPC3TCN6X3L2QNAg","BQADAQADGwADJBlPCwXZKcrRRCXqAg","BQADAQADHQADJBlPC3VtPxEO2id8Ag","BQADAQADHwADJBlPC_YZHY0Wl2kwAg","BQADAQADIQADJBlPC6QTBNilJkBSAg","BQADAQADIwADJBlPC33Te7Pqi42aAg","BQADAQADJQADJBlPC81ZR2VWV9dFAg","BQADAQADJwADJBlPC4dpb_CkeBVGAg","BQADAQADKQADJBlPCxDoL8sfQ1MKAg"
	};
	
	public static void main(String[] args) {
		Log.i("Started Robocat. Build (Commit Hash): " + VERSION_COMMIT_HASH);
		readBotSettings();
		
		Bot bot = new Bot(TELEGRAM_BOT_TOKEN);
		BotHandler botHandler = new BotHandler(bot, BOT_UPDATE_REQUEST_DELAY);
		addBotCommands(botHandler);
		botHandler.start();
	}
	
	private static void readBotSettings() {
		JsonParser parser = new JsonParser();
		try {
			FileReader reader = new FileReader(new File("bot_settings.txt"));
			JsonObject json = parser.parse(reader).getAsJsonObject();
			
			TELEGRAM_BOT_TOKEN = json.get("token").getAsString();
			HELP_COMMAND_TEXT = json.get("help").getAsString();
			BOT_UPDATE_REQUEST_DELAY = json.get("update_request_delay").getAsLong();
		} catch (FileNotFoundException t) {
			Log.e("bot_settings.txt does not exist.", t);
		} catch (JsonParseException t) {
			Log.e("Unable to parse json in bot_settings.txt", t);
		}
	}
	
	private static void addBotCommands(BotHandler botHandler) {
		botHandler.addCommandAlias((Message m) -> {
			botHandler.getBot().sendMessage("" + m.getChatId(), HELP_COMMAND_TEXT, "HTML", true);
		}, "", "команды", "помощь");
		
		botHandler.addCommandAlias((Message m) -> {
			botHandler.getBot().request("sendSticker", new Params()
					.add("chat_id", "" + m.getChatId())
					.add("sticker", Utils.draw(OW_STICKERS)));
		}, "ow", "overwatch", "ов", "овервотч");
		
		botHandler.addCommand("погладить", (Message m) -> {
			botHandler.getBot().sendMessage("" + m.getChatId(), "🐱");
		});
		botHandler.addCommand("амари", (Message m) -> {
			botHandler.getBot().request("sendSticker", new Params()
					.add("chat_id", "" + m.getChatId())
					.add("sticker", Utils.draw(AMARI_STICKERS)));
		});
	}
}
