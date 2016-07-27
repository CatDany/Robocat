package catdany.telegramapi.robocat.logging;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
	
	public static final SimpleDateFormat sdfLog = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static PrintWriter fw;
	
	public static void init() {
		try {
			if (fw != null)
				fw.close();
		} catch (IOException t) {
			Log.e("Unable to initialize file logging.", t);
		}
	}
	
	public static void e(String msg, Throwable t) {
		String log = "[" + sdfLog.format(new Date()) + "] [" + Thread.currentThread().getName() + "] [INFO] " + msg;
		
		System.out.println(log);
		t.printStackTrace(System.err);
		
		logFile(log);
		t.printStackTrace(fw);
	}
	
	public static void i(String msg) {
		String log = "[" + sdfLog.format(new Date()) + "] [" + Thread.currentThread().getName() + "] [INFO] " + msg; 
		System.out.println(log);
		logFile(log);
	}
	
	private static void logFile(String msg) {
		if (fw == null)
			return;
		
		fw.println(msg);
	}
}
