package catdany.telegramapi.robocat.telegram;

import catdany.telegramapi.robocat.utils.Utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Update {
	
	private final int id;
	private Message msg;
	private Message msgEdited;
	private InlineQuery inlineQuery;
	private ChosenInlineResult chosenInlineResult;
	private CallbackQuery callbackQuery;
	
	public Update(JsonElement jsonElement) {
		JsonObject json = jsonElement.getAsJsonObject();
		this.id = Utils.integer(json.get("update_id"));
		this.msg = Utils.msg(json.get("message"));
		this.msgEdited = Utils.msg(json.get("edited_message"));
		this.inlineQuery = new InlineQuery(json.get("inline_query"));
		this.chosenInlineResult = new ChosenInlineResult(json.get("chosen_inline_result"));
		this.callbackQuery = new CallbackQuery(json.get("callback_query"));
	}
	
	public int getId() {
		return id;
	}
	
	public Message getMsg() {
		return msg;
	}
	
	public Message getMsgEdited() {
		return msgEdited;
	}
	
	public Message getActualMessage() {
		return msgEdited != null ? msgEdited : msg;
	}
	
	public CallbackQuery getCallbackQuery() {
		return callbackQuery;
	}
	
	public ChosenInlineResult getChosenInlineResult() {
		return chosenInlineResult;
	}
	
	public InlineQuery getInlineQuery() {
		return inlineQuery;
	}
}
