package catdany.telegramapi.robocat;

import java.io.File;

import catdany.telegramapi.robocat.features.ArmoryData;
import catdany.telegramapi.robocat.features.BattleNetAPI;
import catdany.telegramapi.robocat.features.Pamphlets;
import catdany.telegramapi.robocat.features.WoWToken;
import catdany.telegramapi.robocat.telegram.Message;
import catdany.telegramapi.robocat.utils.Params;
import catdany.telegramapi.robocat.utils.Utils;

public class BotCommands {
	
	public static void init(BotHandler botHandler) {
		botHandler.addCommandAlias((Message m) -> {
			botHandler.getBot().sendMessage("" + m.getChatId(), Main.HELP_COMMAND_TEXT, "HTML", true);
		}, "", "команды", "помощь", "start");
		
		botHandler.addCommandAlias((Message m) -> {
			botHandler.getBot().request("sendSticker", new Params()
					.add("chat_id", "" + m.getChatId())
					.add("sticker", Utils.draw(Main.OW_STICKERS)));
		}, "ow", "overwatch", "ов", "овервотч");
		
		botHandler.addCommand("погладить", (Message m) -> {
			botHandler.getBot().sendMessage("" + m.getChatId(), "🐱");
		});
		
		botHandler.addCommand("амари", (Message m) -> {
			botHandler.getBot().request("sendSticker", new Params()
					.add("chat_id", "" + m.getChatId())
					.add("sticker", Utils.draw(Main.AMARI_STICKERS)));
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
			} else if (collectedId >= 0) {
				StringBuilder str = pamphletsInfo(data);
				botHandler.getBot().sendMessage("" + m.getFrom().getId(), str.toString());
			}
		});
		
		botHandler.addCommand("листовки", (Message m) -> {
			Pamphlets data = Pamphlets.getDataFor(m.getFrom().getId());
			if (data.countObtained() == Pamphlets.pamphletNames.length) {
				botHandler.getBot().sendMessage("" + m.getChatId(), m.getFrom().getFullName() + " имеет достижение <b>[Вот теперь все ясно]</b>!", "HTML", false);
			} else {
				StringBuilder str = pamphletsInfo(data);
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
			int timeLeft = Main.LEGION_LAUNCH_TIME - (int)(System.currentTimeMillis()/1000);
			int days = timeLeft / (60*60*24);
			int hours = timeLeft % (60*60*24) / (60*60);
			int minutes = timeLeft % (60*60) / 60;
			int seconds = timeLeft % 60;
			botHandler.getBot().sendMessage("" + m.getChatId(), String.format("Терпение! Осталось всего %s д %s ч %s м %s с.", days, hours, minutes, seconds));
			
		}, "легион", "скореебылегион");
		
		botHandler.addCommandAlias((Message m) -> {
			WoWToken.update();
			botHandler.getBot().sendMessage("" + m.getChatId(), "Цена жетона WoW (EU): <b>" + WoWToken.price + "</b>\nНаименьшая цена за 24 ч: <b>" + WoWToken.min24 + "</b>\nНаибольшая цена за 24 ч: <b>" + WoWToken.max24 + "</b>", "HTML", false);
			botHandler.getBot().sendPhoto("" + m.getChatId(), new File("wowtoken.png"));
		}, "токен", "жетон");
		
		botHandler.addCommand("армори", (Message m) -> {
			String[] args = m.getText().split(" ", 3);
			if (args.length < 3) {
				botHandler.getBot().sendMessage("" + m.getChatId(), "Пример использования команды:\n\n/армори Умбрик Свежеватель душ");
			} else {
				String character = args[1];
				String realm = args[2].replace(' ', '-');
				ArmoryData armory = BattleNetAPI.requestArmoryData(realm, character);
				if (armory != null) {
					botHandler.getBot().sendMessage("" + m.getChatId(),
							"<a href='" + armory.getArmoryLink() + "'>" + armory.getName() + "-" + armory.getRealm().replace(" ", "") + "</a>\n"
						  + "<b>" + armory.getLevel() + "</b> " + armory.getLocalizedRace() + "-" + armory.getLocalizedClass() + "\n"
						  + "" + armory.getLocalizedFaction() + "\n"
						  + "🏆 " + armory.getAchievementPoints() + " очков\n"
						  + "⚔ " + armory.getTotalHonorableKills() + " ПП"
							, "HTML", false);
				} else {
					botHandler.getBot().sendMessage("" + m.getChatId(), "Персонаж " + character + "-" + realm + " не существует.");
				}
			}
		});
	}
	
	private static StringBuilder pamphletsInfo(Pamphlets data) {
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
		return str;
	}
}
