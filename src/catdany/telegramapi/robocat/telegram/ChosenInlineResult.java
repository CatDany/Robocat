package catdany.telegramapi.robocat.telegram;

import com.google.gson.JsonElement;

public class ChosenInlineResult {
	
	private JsonElement json;
	
	public ChosenInlineResult(JsonElement json) {
		this.json = json;
	}
	
	public JsonElement getJson() {
		return json;
	}
}
