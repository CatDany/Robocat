package catdany.telegramapi.robocat.pamphlets;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import catdany.telegramapi.robocat.logging.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

public class Pamphlets {
	
	private static HashMap<Integer, Pamphlets> pamphletDatabase = new HashMap<Integer, Pamphlets>();
	
	public static final String[] pamphletNames = new String[] {
		"Рваная листовка", "Подмоченная листовка", "Благоухающая листовка", "Листовка с каракулями", "Зловещая листовка", "Почерневшая листовка", "Истрепанная листовка", "Мятая листовка", "Листовка с пророчеством", "Заляпанная листовка", "Демоническая листовка", "Почерканная листовка"
	};
	
	public static final long PAMPHLET_PERSONAL_COOLDOWN = 15*60*1000;
	
	private long lastPamphletCollectedAt = 0;
	private boolean[] pamphlets = new boolean[pamphletNames.length];
	
	private Pamphlets(JsonArray boolArray, long lastPamphletCollectedAt) {
		this.lastPamphletCollectedAt = lastPamphletCollectedAt;
		for (int i = 0; i < boolArray.size(); i++) {
			this.pamphlets[i] = boolArray.get(i).getAsBoolean();
		}
	}
	
	public Pamphlets() {}
	
	public static Pamphlets getDataFor(int userId) {
		if (!pamphletDatabase.containsKey(userId)) {
			pamphletDatabase.put(userId, new Pamphlets());
		}
		return pamphletDatabase.get(userId);
	}
	
	public static HashMap<Integer, Pamphlets> fromFile(File file) {
		HashMap<Integer, Pamphlets> map = new HashMap<Integer, Pamphlets>();
		JsonParser parser = new JsonParser();
		try {
			FileReader reader = new FileReader(file);
			JsonArray json = parser.parse(reader).getAsJsonArray();
			reader.close();
			for (int i = 0; i < json.size(); i++) {
				JsonObject entry = json.get(i).getAsJsonObject();
				map.put(entry.get("user_id").getAsInt(), new Pamphlets(entry.get("pamphlets").getAsJsonArray(), entry.get("last_collected").getAsLong()));
			}
			return map;
		} catch (FileNotFoundException t) {
			Log.e(file.getName() + " does not exist.", t);
		} catch (JsonParseException | IOException t) {
			Log.e("Unable to parse json in " + file.getName(), t);
		}
		return new HashMap<Integer, Pamphlets>();
	}
	
	public static void toFile(File file, HashMap<Integer, Pamphlets> map) {
		JsonArray json = new JsonArray();
		for (Entry<Integer, Pamphlets> i : map.entrySet()) {
			JsonObject entry = new JsonObject();
			entry.addProperty("user_id", i.getKey());
			entry.addProperty("last_collected", i.getValue().lastPamphletCollectedAt);
			JsonArray boolArray = new JsonArray();
			for (int k = 0; k < pamphletNames.length; k++) {
				boolArray.add(i.getValue().pamphlets[k]);
			}
			entry.add("pamphlets", boolArray);
			json.add(entry);
		}
		
		try {
			FileWriter fw = new FileWriter(file);
			fw.write(json.toString());
			fw.close();
		} catch (IOException t) {
			Log.e("Unable to write pamphlets database to file: " + file.getName(), t);
		}
	}
	
	public static synchronized void save() {
		toFile(new File("pamphlets.json"), pamphletDatabase);
	}
	
	public static synchronized void load() {
		pamphletDatabase = fromFile(new File("pamphlets.json"));
	}
	
	public List<String> getObtainedPamphlets() {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < pamphlets.length; i++) {
			if (pamphlets[i]) {
				list.add(pamphletNames[i]);
			}
		}
		return list;
	}
	
	public List<String> getUnobtainedPamphlets() {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < pamphlets.length; i++) {
			if (!pamphlets[i]) {
				list.add(pamphletNames[i]);
			}
		}
		return list;
	}
	
	public int countObtained() {
		int count = 0;
		for (boolean i : pamphlets) {
			if (i) {
				count++;
			}
		}
		return count;
	}
	
	/**
	 * Attempt to collect a pamphlet.
	 * @return ID of collected pamphlet (0-11) or <code>-1</code> if on cooldown.
	 */
	public int collect() {
		if (lastPamphletCollectedAt + PAMPHLET_PERSONAL_COOLDOWN < System.currentTimeMillis()) {
			int randomId = (int)(Math.random() * 12);
			pamphlets[randomId] = true;
			lastPamphletCollectedAt = System.currentTimeMillis();
			save();
			return randomId;
		} else {
			return -1;
		}
	}
}
