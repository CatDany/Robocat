package catdany.telegramapi.robocat.features;

import catdany.telegramapi.robocat.utils.Utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ArmoryData {
	
	private long lastModified;
	private String name;
	private String realm;
	private String battlegroup;
	private ClassEnum clazz;
	private RaceEnum race;
	private int gender;
	private int level;
	private int achievementPoints;
	private String thumbnail;
	private FactionEnum faction;
	private int totalHonorableKills;
	
	public ArmoryData(JsonElement jsonElement) {
		JsonObject json = jsonElement.getAsJsonObject();
		this.lastModified = Utils.longint(json.get("lastModified"));
		this.name = Utils.string(json.get("name"));
		this.realm = Utils.string(json.get("realm"));
		this.battlegroup = Utils.string(json.get("battlegroup"));
		this.clazz = ClassEnum.fromId(Utils.integer(json.get("class")));
		this.race = RaceEnum.fromId(Utils.integer(json.get("race")));
		this.gender = Utils.integer(json.get("gender"));
		this.level = Utils.integer(json.get("level"));
		this.achievementPoints = Utils.integer(json.get("achievementPoints"));
		this.thumbnail = Utils.string(json.get("thumbnail"));
		this.faction = FactionEnum.fromId(Utils.integer(json.get("faction")));
		this.totalHonorableKills = Utils.integer(json.get("totalHonorableKills"));;
	}
	
	public int getAchievementPoints() {
		return achievementPoints;
	}
	
	public String getBattlegroup() {
		return battlegroup;
	}
	
	public ClassEnum getClazz() {
		return clazz;
	}
	
	public String getLocalizedClass() {
		return clazz.getLocalized(getGender());
	}
	
	public int getGender() {
		return gender;
	}
	
	public FactionEnum getFaction() {
		return faction;
	}
	
	public String getLocalizedFaction() {
		return faction.localizedName;
	}
	
	public long getLastModified() {
		return lastModified;
	}
	
	public int getLevel() {
		return level;
	}
	
	public String getName() {
		return name;
	}
	
	public String getRealm() {
		return realm;
	}
	
	public RaceEnum getRace() {
		return race;
	}
	
	public String getLocalizedRace() {
		return race.getLocalized(getGender());
	}
	
	public String getThumbnailURL() {
		return BattleNetAPI.ARMORY_THUMBNAIL + thumbnail;
	}
	
	public int getTotalHonorableKills() {
		return totalHonorableKills;
	}
	
	public String getArmoryLink() {
		return String.format(BattleNetAPI.ARMORY_LINK, realm, name);
	}
	
	public static enum ClassEnum {
		
		WARRIOR("Воин"),
		PALADIN("Паладин"),
		HUNTER("Охотник", "Охотница"),
		ROGUE("Разбойник", "Разбойница"),
		PRIEST("Жрец", "Жрица"),
		DEATHKNIGHT("Рыцарь смерти"),
		SHAMAN("Шаман", "Шаманка"),
		MAGE("Маг"),
		WARLOCK("Чернокнижник", "Чернокнижница"),
		MONK("Монах", "Монахиня"),
		DRUID("Друид"),
		DEMONHUNTER("Охотник на демонов", "Охотница на демонов");
		

		public final String localizedName;
		public final String localizedNameFemale;
		
		private ClassEnum(String localizedName, String localizedNameFemale) {
			this.localizedName = localizedName;
			this.localizedNameFemale = localizedNameFemale;
		}
		
		private ClassEnum(String localizedName) {
			this(localizedName, localizedName);
		}
		
		public String getLocalized(int gender) {
			return gender == 0 ? localizedName : localizedNameFemale;
		}
		
		public static ClassEnum fromId(int id) {
			switch (id) {
			case 1:
				return WARRIOR;
			case 2:
				return PALADIN;
			case 3:
				return HUNTER;
			case 4:
				return ROGUE;
			case 5:
				return PRIEST;
			case 6:
				return DEATHKNIGHT;
			case 7:
				return SHAMAN;
			case 8:
				return MAGE;
			case 9:
				return WARLOCK;
			case 10:
				return MONK;
			case 11:
				return DRUID;
			case 12:
				return DEMONHUNTER;
			}
			return null;
		}
	}
	
	public static enum RaceEnum {
		
		HUMAN("Человек"),
		ORC("Орк"),
		DWARF("Дворф"),
		NELF("Ночной эльф", "Ночная эльфийка"),
		UNDEAD("Нежить"),
		TAUREN("Таурен"),
		GNOME("Гном"),
		TROLL("Тролль"),
		BELF("Эльф крови", "Эльфийка крови"),
		DRAENEI("Дреней", "Дренейка"),
		WORGEN("Ворген"),
		GOBLIN("Гоблин"),
		PANDAREN("Пандарен");
		
		public final String localizedName;
		public final String localizedNameFemale;
		
		private RaceEnum(String localizedName, String localizedNameFemale) {
			this.localizedName = localizedName;
			this.localizedNameFemale = localizedNameFemale;
		}
		
		private RaceEnum(String localizedName) {
			this(localizedName, localizedName);
		}
		
		public String getLocalized(int gender) {
			return gender == 0 ? localizedName : localizedNameFemale;
		}
		
		public static RaceEnum fromId(int id) {
			switch (id) {
			case 1:
				return HUMAN;
			case 2:
				return ORC;
			case 3:
				return DWARF;
			case 4:
				return NELF;
			case 5:
				return UNDEAD;
			case 6:
				return TAUREN;
			case 7:
				return GNOME;
			case 8:
				return TROLL;
			case 9:
				return GOBLIN;
			case 10:
				return BELF;
			case 11:
				return DRAENEI;
			case 22:
				return WORGEN;
			case 24:
			case 25:
			case 26:
				return PANDAREN;
			}
			return null;
		}
	}
	
	public static enum FactionEnum {
		
		ALLIANCE("🔵 Альянс"),
		HORDE("🔴 Орда");
		
		public final String localizedName;
		
		private FactionEnum(String localizedName) {
			this.localizedName = localizedName;
		}
		
		public static FactionEnum fromId(int id) {
			switch (id) {
			case 0:
				return ALLIANCE;
			case 1:
				return HORDE;
			}
			return null;
		}
		
	}
}
