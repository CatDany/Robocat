package catdany.telegramapi.robocat;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class APIResponse {
	
	private final JsonObject json;
	
	private boolean ok;
	private int errorCode;
	private String errorDesc;
	
	public APIResponse(JsonElement json) {
		if (json == null) {
			this.ok = false;
			this.json = null;
		} else {
			this.json = json.getAsJsonObject();
			
			this.ok = this.json.get("ok").getAsBoolean();
			if (!ok) {
				this.errorCode = this.json.get("error_code").getAsInt();
				this.errorDesc = this.json.get("description").getAsString();
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
}
