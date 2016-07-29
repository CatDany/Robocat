package catdany.telegramapi.robocat.features;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import catdany.telegramapi.robocat.logging.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

public class Blacklist {
	
	public static ArrayList<Integer> blacklistedIds = new ArrayList<Integer>();
	
	public static boolean isBlacklisted(int id) {
		return blacklistedIds.contains(id);
	}
	
	public static void load() {
		JsonParser parser = new JsonParser();
		File file = new File("blacklist.json");
		try {
			FileReader reader = new FileReader(file);
			JsonArray json = parser.parse(reader).getAsJsonArray();
			reader.close();
			
			for (int i = 0; i < json.size(); i++) {
				blacklistedIds.add(json.get(i).getAsInt());
			}
		} catch (FileNotFoundException t) {
			try {
				file.createNewFile();
				FileWriter fw = new FileWriter(file);
				fw.write(new JsonArray().toString());
				fw.close();
			} catch (IOException t0) {
			Log.e("Unable to create blacklist.json", t);
			}
		} catch (JsonParseException | IOException t) {
			Log.e("Unable to load blacklist.json", t);
		}
	}
	
	public static void save() {
		JsonArray json = new JsonArray();
		for (Integer i : blacklistedIds) {
			json.add(i);
		}
		
		try {
			FileWriter fw = new FileWriter(new File("blacklist.json"));
			fw.write(json.toString());
			fw.close();
		} catch (IOException t) {
			Log.e("Unable to save blacklist.json", t);
		}
	}
	
	public static boolean add(int id) {
		boolean r = blacklistedIds.add(id);
		save();
		return r;
	}
	
	public static boolean remove(int id) {
		if (blacklistedIds.contains(id)) {
			blacklistedIds.remove(id);
			save();
			return true;
		} else {
			return false;
		}
	}
}
