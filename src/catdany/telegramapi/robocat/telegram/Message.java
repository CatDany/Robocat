package catdany.telegramapi.robocat.telegram;

import catdany.telegramapi.robocat.utils.Utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Message {
	
	private final int id;
	private long date;
	private String text;
	private User from;
	private JsonObject json;
	
	public Message(JsonElement json) {
		this.json = json.getAsJsonObject();
		
		this.id = Utils.integer(this.json.get("message_id"));
		this.date = Utils.longint(this.json.get("date"));
		this.text = Utils.string(this.json.get("text"));
		this.from = Utils.user(this.json.get("from"));
	}
	
	public JsonElement get(String property) {
		return json.get(property);
	}
	
	public int getId() {
		return id;
	}
	
	public long getDate() {
		return date;
	}
	
	public String getText() {
		return text;
	}
	
	public User getFrom() {
		return from;
	}
	
	public Message getReplyTo() {
		return Utils.msg(json.get("reply_to_message"));
	}
	
	public int getChatId() {
		return json.get("chat").getAsJsonObject().get("id").getAsInt();
	}
	
	@Override
	public String toString() {
		return json.toString();
	}
}
