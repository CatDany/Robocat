package catdany.telegramapi.robocat.telegram;

import catdany.telegramapi.robocat.utils.Utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Photo {
	
	private String fileId;
	private int width;
	private int height;
	private int fileSize;
	
	public Photo(JsonElement jsonElement) {
		JsonObject json = jsonElement.getAsJsonObject();
		
		this.fileId = Utils.string(json.get("file_id"));
		this.width = Utils.integer(json.get("width"));
		this.height = Utils.integer(json.get("height"));
		this.fileSize = Utils.integer(json.get("file_size"));
	}
	
	public String getFileId() {
		return fileId;
	}
	
	public int getFileSize() {
		return fileSize;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
}
