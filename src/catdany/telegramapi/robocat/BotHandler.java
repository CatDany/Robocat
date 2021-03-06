package catdany.telegramapi.robocat;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import catdany.telegramapi.robocat.features.Blacklist;
import catdany.telegramapi.robocat.logging.Log;
import catdany.telegramapi.robocat.telegram.Message;
import catdany.telegramapi.robocat.telegram.Update;
import catdany.telegramapi.robocat.utils.Params;
import catdany.telegramapi.robocat.utils.Utils;

import com.google.gson.JsonObject;

public class BotHandler implements Runnable {

	private ScheduledExecutorService exec;
	private long updateRequestTimeoutMillis;
	
	private Bot bot;
	private HashMap<String, Consumer<Message>> commands;
	private HashMap<String, Consumer<Message>> replies;
	
	private int botUserId;
	private String botUsername;
	private String botName;
	
	public BotHandler(Bot bot, long updateRequestTimeoutMillis) {
		this.bot = bot;
		this.updateRequestTimeoutMillis = updateRequestTimeoutMillis;
		this.commands = new HashMap<String, Consumer<Message>>();
		this.replies = new HashMap<String, Consumer<Message>>();
		
		this.exec = Executors.newSingleThreadScheduledExecutor();
	}
	
	public void start() {
		APIResponse reGetMe = bot.request("getMe", new Params());
		if (reGetMe.isOK()) {
			JsonObject jsonGetMe = reGetMe.getResultAsObject();
			this.botUserId = Utils.integer(jsonGetMe.get("id"));
			this.botUsername = Utils.string(jsonGetMe.get("username"));
			this.botName = Utils.string(jsonGetMe.get("first_name"));
			Log.i("Logged in as @" + botUsername + " #" + botUserId);

			Log.i("Update request timeout is set to " + updateRequestTimeoutMillis + " ms.");
			exec.scheduleAtFixedRate(this, updateRequestTimeoutMillis, updateRequestTimeoutMillis, TimeUnit.MILLISECONDS);
		} else {
			Log.e("getMe failed.", null);
		}
	}
	
	@Override
	public void run() {
		try {
			Update[] upd = bot.getUpdates();
			for (Update i : upd) {
				String cmd = i.getActualMessage().getText();
				if (cmd != null && cmd.startsWith("/") && !Blacklist.isBlacklisted(i.getActualMessage().getFrom().getId())) {
					cmd = cmd.split(" ")[0].substring(1);
					if (commands.containsKey(cmd)) {
						Log.i("Executing command /" + cmd + " from user @" + i.getActualMessage().getFrom().getUsername() + " [" + i.getActualMessage().getFrom().getFullName() + "] #" + i.getActualMessage().getFrom().getId());
						commands.get(cmd).accept(i.getActualMessage());
					}
				} else {
					if (i.getActualMessage().getReplyTo() != null) {
						String replyText = i.getActualMessage().getReplyTo().getText();
						if (replies.containsKey(replyText)) {
							Log.i("Executing reply from user @" + i.getActualMessage().getFrom().getUsername() + " [" + i.getActualMessage().getFrom().getFullName() + "] #" + i.getActualMessage().getFrom().getId() + " -> " + replyText);
							replies.get(replyText).accept(i.getActualMessage());
						}
					}
				}
			}
		} catch (Exception t) {
			Log.e("Exception caught while handling updates.", t);
		}
	}
	
	public Bot getBot() {
		return bot;
	}
	
	public String getBotName() {
		return botName;
	}
	
	public int getBotUserId() {
		return botUserId;
	}
	
	public String getBotUsername() {
		return botUsername;
	}
	
	public boolean addCommand(String command, Consumer<Message> r) {
		if (commands.containsKey(command)) {
			return false;
		} else {
			commands.put(command, r);
			return true;
		}
	}
	
	public void addCommandAlias(Consumer<Message> r, String... aliases) {
		for (String i : aliases) {
			addCommand(i, r);
		}
	}
	
	public boolean removeCommand(String command) {
		if (commands.containsKey(command)) {
			commands.put(command, null);
			return true;
		} else {
			return false;
		}
	}
	
	public boolean addReply(String replyText, Consumer<Message> r) {
		if (replies.containsKey(replyText)) {
			return false;
		} else {
			replies.put(replyText, r);
			return true;
		}
	}
	
	public boolean removeReply(String replyText) {
		if (replies.containsKey(replyText)) {
			replies.put(replyText, null);
			return true;
		} else {
			return false;
		}
	}
}
