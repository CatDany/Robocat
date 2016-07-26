package catdany.telegramapi.robocat.telegram;

import catdany.telegramapi.robocat.utils.Utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class User {
	
	private final int id;
	private String firstName;
	private String lastName;
	private String username;
	
	public User(JsonElement jsonElement) {
		JsonObject json = jsonElement.getAsJsonObject();
		this.id = Utils.integer(json.get("id"));
		this.firstName = Utils.string(json.get("first_name"));
		this.lastName = Utils.string(json.get("last_name"));
		this.username = Utils.string(json.get("username"));
	}
	
	public int getId() {
		return id;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getFullName() {
		if (firstName != null && lastName != null)
			return firstName + " " + lastName;
		else if (firstName != null && lastName == null)
			return firstName;
		else if (username != null)
			return "@" + username;
		else
			return "#" + id;
	}
	
	public String getUsername() {
		return username;
	}
}
