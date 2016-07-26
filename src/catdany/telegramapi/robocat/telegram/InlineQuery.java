package catdany.telegramapi.robocat.telegram;

import com.google.gson.JsonElement;

public class InlineQuery {
	
	private JsonElement json;
	
	public InlineQuery(JsonElement json) {
		this.json = json;
	}
	
	public JsonElement getJson() {
		return json;
	}
}
