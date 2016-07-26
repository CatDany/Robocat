package catdany.telegramapi.robocat.telegram;

import com.google.gson.JsonElement;

public class CallbackQuery {
	
	private JsonElement json;
	
	public CallbackQuery(JsonElement json) {
		this.json = json;
	}
	
	public JsonElement getJson() {
		return json;
	}
}
