package catdany.telegramapi.robocat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import catdany.telegramapi.robocat.logging.Log;
import catdany.telegramapi.robocat.pamphlets.Pamphlets;
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
	
	public static final int LEGION_LAUNCH_TIME = 1472594400;
	
	public static void main(String[] args) {
		Log.i("Started Robocat. Build (Commit Hash): " + VERSION_COMMIT_HASH);
		readBotSettings();
		Pamphlets.load();
		
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
			reader.close();
			
			TELEGRAM_BOT_TOKEN = json.get("token").getAsString();
			HELP_COMMAND_TEXT = json.get("help").getAsString();
			BOT_UPDATE_REQUEST_DELAY = json.get("update_request_delay").getAsLong();
		} catch (FileNotFoundException t) {
			Log.e("bot_settings.txt does not exist.", t);
		} catch (JsonParseException | IOException t) {
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
		
		botHandler.addCommand("листовка", (Message m) -> {
			Pamphlets data = Pamphlets.getDataFor(m.getFrom().getId());
			int collectedId = data.collect();
			String msg = m.getFrom().getFullName() + ", у тебя аура существа, которое любит задавать вопросы.";
			if (collectedId >= 0)
				msg = m.getFrom().getFullName() + " получает предмет: <i>[" + Pamphlets.pamphletNames[collectedId] + "]</i>.";
			botHandler.getBot().sendMessage("" + m.getChatId(), msg, "HTML", false);
			
			if (data.countObtained() == Pamphlets.pamphletNames.length) {
				botHandler.getBot().sendMessage("" + m.getChatId(), m.getFrom().getFullName() + " получает достижение <b>[Вот теперь все ясно]</b>!", "HTML", false);
			}
		});
		
		botHandler.addCommand("листовки", (Message m) -> {
			Pamphlets data = Pamphlets.getDataFor(m.getFrom().getId());
			if (data.countObtained() == Pamphlets.pamphletNames.length) {
				botHandler.getBot().sendMessage("" + m.getChatId(), m.getFrom().getFullName() + " собрал(а) имеет достижение <b>[Вот теперь все ясно]</b>!", "HTML", false);
			} else {
				StringBuilder str = new StringBuilder();
				str.append("Собранные листовки:\n");
				for (String i : data.getObtainedPamphlets()) {
					str.append("- " + i + "\n");
				}
				str.append("\nЕще не собранные листовки:\n");
				for (String i : data.getUnobtainedPamphlets()) {
					str.append("- " + i + "\n");
				}
				str.append("\nНапишите /листовка, чтобы собрать листовку.");
				APIResponse r0 = botHandler.getBot().sendMessage("" + m.getFrom().getId(), str.toString());
				if (!r0.isOK()) {
					botHandler.getBot().sendMessage("" + m.getChatId(), "Невозможно выполнить команду /листовки. Необходимо добавить бота в список друзей.");
				}
			}
		});
		
		botHandler.addCommandAlias((Message m) -> {
			int rand = (int)(Math.random() * 100);
			botHandler.getBot().sendMessage("" + m.getChatId(), "<i>" + m.getFrom().getFullName() + " выбрасывает " + rand + " (0-100)</i>", "HTML", false);
		}, "число", "roll");
		
		botHandler.addCommandAlias((Message m) -> {
			int timeLeft = LEGION_LAUNCH_TIME - (int)(System.currentTimeMillis()/1000);
			int days = timeLeft / (60*60*24);
			int hours = timeLeft % (60*60*24) / (60*60);
			int minutes = timeLeft % (60*60) / 60;
			int seconds = timeLeft % 60;
			botHandler.getBot().sendMessage("" + m.getChatId(), String.format("Терпение! Осталось всего %s д %s ч %s м %s с.", days, hours, minutes, seconds));
			
		}, "легион", "скореебылегион");
	}
}
