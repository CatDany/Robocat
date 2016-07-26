package catdany.telegramapi.robocat;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class APIResponse {
	
	private final JsonObject json;
	
	private boolean ok;
	private int errorCode;
	private String errorDesc;
	private TelegramException exception;
	
	public APIResponse(JsonElement json) {
		if (json == null) {
			this.ok = false;
			this.json = null;
			this.exception = new TelegramException(null);
		} else {
			this.json = json.getAsJsonObject();
			
			this.ok = this.json.get("ok").getAsBoolean();
			if (!ok) {
				this.errorCode = this.json.get("error_code").getAsInt();
				this.errorDesc = this.json.get("description").getAsString();
				this.exception = new TelegramException(this);
			}
		}
	}
	
	public int getErrorCode() {
		return errorCode;
	}
	
	public String getErrorDesc() {
		return errorDesc;
	}
	
	public boolean isOK() {
		return ok;
	}
	
	public JsonElement getResult() {
		return json.get("result");
	}
	
	public JsonArray getResultAsArray() {
		return getResult().getAsJsonArray();
	}
	
	public JsonObject getResultAsObject() {
		return getResult().getAsJsonObject();
	}
	
	public Exception getException() {
		return exception;
	}
}
