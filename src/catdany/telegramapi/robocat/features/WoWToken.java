package catdany.telegramapi.robocat.features;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import catdany.telegramapi.robocat.logging.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class WoWToken {
	
	public static final String WOW_TOKEN_API = "https://wowtoken.info/wowtoken.json";
	public static final SimpleDateFormat sdfDate = new SimpleDateFormat("dd MMMM");
	public static final SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
	
	public static int price;
	public static int min24;
	public static int max24;
	public static long timestamp;
	
	public static void update() {
		if (System.currentTimeMillis()/1000 - timestamp > 60*10) {
			Log.i("Fetching data WoWToken.info API.");
			forceUpdateTokenPrice();
		}
	}
	
	private static void forceUpdateTokenPrice() {
		try {
			URL url = new URL(WOW_TOKEN_API);
			InputStreamReader isr = new InputStreamReader(url.openStream());
			JsonParser parser = new JsonParser();
			JsonElement json = parser.parse(isr);
			isr.close();
			
			// Fetching data
			JsonObject eu = json.getAsJsonObject().get("update").getAsJsonObject().get("EU").getAsJsonObject();
			JsonObject raw = eu.get("raw").getAsJsonObject();
			price = raw.get("buy").getAsInt();
			min24 = raw.get("24min").getAsInt();
			max24 = raw.get("24max").getAsInt();
			timestamp = eu.get("timestamp").getAsLong();
			
			// Downloading the graph
			String sparkurl = eu.get("formatted").getAsJsonObject().get("sparkurl").getAsString();
			String sparkurl0 = sparkurl.substring(0, sparkurl.indexOf("chtt=") + 5);
			String sparkurl1 = sparkurl.substring(sparkurl.indexOf('&', sparkurl.indexOf("chtt=")));
			sparkurl = sparkurl0 + URLEncoder.encode("График цены жетона WoW-EU за последние сутки.|" + formatDate(timestamp), "UTF-8") + sparkurl1;
			URL urlSpark = new URL(sparkurl);
			InputStream sparkin = urlSpark.openStream();
			FileOutputStream fos = new FileOutputStream(new File("wowtoken.png"));
			byte[] buf = new byte[1024];
			int len;
			while ((len = sparkin.read(buf)) >= 0) {
				fos.write(buf, 0, len);
			}
			fos.close();
			sparkin.close();
		} catch (IOException t) {
			Log.e("Unable to perform WoWToken.info API request.", t);
		}
	}
	
	private static String formatDate(long timestamp) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timestamp*1000);
		
		StringBuilder str = new StringBuilder();;
		sdfDate.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
		sdfTime.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
		
		cal.add(Calendar.DAY_OF_MONTH, -1);
		str.append(sdfDate.format(cal.getTime()));
		str.append(" - ");
		cal.add(Calendar.DAY_OF_MONTH, 1);
		str.append(sdfDate.format(cal.getTime()));
		str.append(" ");
		str.append(sdfTime.format(cal.getTime()));
		str.append(" МСК");
		
		return str.toString();
	}
}