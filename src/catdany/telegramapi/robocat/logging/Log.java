package catdany.telegramapi.robocat.logging;

public class Log {
	
	public static void e(String msg, Throwable t) {
		System.err.println(msg);
		t.printStackTrace(System.err);
	}
	
	public static void i(String msg) {
		System.out.println(msg);
	}
	
}
