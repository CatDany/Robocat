package catdany.telegramapi.robocat;

import java.awt.image.BufferedImage;
import java.io.File;

import catdany.telegramapi.robocat.features.ArmoryData;
import catdany.telegramapi.robocat.features.BattleNetAPI;
import catdany.telegramapi.robocat.features.ImageEditor;
import catdany.telegramapi.robocat.features.Pamphlets;
import catdany.telegramapi.robocat.features.WoWToken;
import catdany.telegramapi.robocat.telegram.Message;
import catdany.telegramapi.robocat.telegram.Photo;
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
			if (data.countObtained() == Pamphlets.pamphletNames.length) {
				botHandler.getBot().sendMessage("" + m.getChatId(), m.getFrom().getFullName() + " уже собрал все листовки и получил достижение <a href='http://ru.wowhead.com/achievement=11065'>[Вот теперь все ясно]</a>.", "HTML", true);
			} else {
				int collectedId = data.collect();
				
				String msg;
				if (collectedId >= 0) {
					msg = m.getFrom().getFullName() + " получает предмет: <i>[" + Pamphlets.pamphletNames[collectedId] + "]</i>. ";
					int pamphletsLeft = Pamphlets.pamphletNames.length - data.countObtained();
					if (pamphletsLeft > 0)
						msg += Utils.amount(pamphletsLeft, "Осталась %s листовка", "Осталось %s листовки", "Осталось %s листовок") + ".";
				}
				else
					msg = m.getFrom().getFullName() + ", у тебя аура существа, которое любит задавать вопросы.";
				botHandler.getBot().sendMessage("" + m.getChatId(), msg, "HTML", false);
				
				if (data.countObtained() == Pamphlets.pamphletNames.length) {
					botHandler.getBot().sendMessage("" + m.getChatId(), m.getFrom().getFullName() + " получает достижение <a href='http://ru.wowhead.com/achievement=11065'>[Вот теперь все ясно]</a>!", "HTML", true);
				} else if (collectedId >= 0) {
					StringBuilder str = pamphletsInfo(data);
					str.append("\nНапишите /листовка, чтобы собрать листовку.");
					botHandler.getBot().sendMessage("" + m.getFrom().getId(), str.toString());
				}
			}
		});
		
		botHandler.addCommand("листовки", (Message m) -> {
			Pamphlets data = Pamphlets.getDataFor(m.getFrom().getId());
			if (data.countObtained() == Pamphlets.pamphletNames.length) {
				botHandler.getBot().sendMessage("" + m.getChatId(), m.getFrom().getFullName() + " собрал все листовки и получил достижение <a href='http://ru.wowhead.com/achievement=11065'>[Вот теперь все ясно]</a>.", "HTML", true);
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
			botHandler.getBot().sendMessage("" + m.getChatId(), "" + m.getFrom().getFullName() + " выбрасывает " + rand + " (0-100)", "HTML", false);
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
		
		final String replyTextFlipRight = "Ответьте на это сообщение фотографией, которую хотите отразить слева направо.";
		botHandler.addCommand("шреш", (Message m) -> {
			botHandler.getBot().sendMessage("" + m.getChatId(), replyTextFlipRight);
		});
		botHandler.addReply(replyTextFlipRight, (Message m) -> {
			doFlip(m, false, botHandler);
		});
		
		final String replyTextFlipLeft = "Ответьте на это сообщение фотографией, которую хотите отразить справа налево.";
		botHandler.addCommand("кек", (Message m) -> {
			botHandler.getBot().sendMessage("" + m.getChatId(), replyTextFlipLeft);
		});
		botHandler.addReply(replyTextFlipLeft, (Message m) -> {
			doFlip(m, true, botHandler);
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
		return str;
	}
	
	private static void doFlip(Message m, boolean rightToLeft, BotHandler botHandler) {
		Photo[] photo = m.getPhoto();
		if (photo.length == 0) {
			botHandler.getBot().sendMessage("" + m.getChatId(), "Вы не прикрепили фото. Возможно, потому что Вы отправили фотографию файлом.", "", false, m.getId());
			return;
		}
		Photo best = ImageEditor.getBestPhoto(photo);
		if (best == null) {
			botHandler.getBot().sendMessage("" + m.getChatId(), "Фотография слишком большая!", "", false, m.getId());
			return;
		}
		BufferedImage img = ImageEditor.loadImage(best, botHandler.getBot()).get();
		if (img == null) {
			botHandler.getBot().sendMessage("" + m.getChatId(), "Что-то пошло не так.", "", false, m.getId());
			return;
		}
		
		if (rightToLeft)
			ImageEditor.flipLeft(img);
		else
			ImageEditor.flipRight(img);
		
		File tmpOut = ImageEditor.saveSilently(img);
		if (tmpOut == null) {
			botHandler.getBot().sendMessage("" + m.getChatId(), "Что-то пошло не так.", "", false, m.getId());
			return;
		}
		botHandler.getBot().sendPhoto("" + m.getChatId(), tmpOut);
		tmpOut.delete();
	}
}
