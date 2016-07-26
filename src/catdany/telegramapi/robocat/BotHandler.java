package catdany.telegramapi.robocat;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

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
	
	private int botUserId;
	private String botUsername;
	private String botName;
	
	public BotHandler(Bot bot, long updateRequestTimeoutMillis) {
		this.bot = bot;
		this.updateRequestTimeoutMillis = updateRequestTimeoutMillis;
		this.commands = new HashMap<String, Consumer<Message>>();
		
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
				if (cmd != null && cmd.startsWith("/")) {
					cmd = cmd.substring(1);
					if (commands.containsKey(cmd)) {
						Log.i("Executing command /" + cmd + " from user @" + i.getActualMessage().getFrom().getUsername());
						commands.get(cmd).accept(i.getActualMessage());
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
}
