package catdany.telegramapi.robocat;

public class TelegramException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8829174700722035005L;
	
	private int errorCode;
	private String errorDesc;
	
	public TelegramException(APIResponse response) {
		if (response == null) {
			errorCode = 0;
		}
		else {
			this.errorCode = response.getErrorCode();
			this.errorDesc = response.getErrorDesc();
		}
	}
	
	public String getMessage() {
		if (errorCode == 0)
			return "Telegram API Error: null response";
		else
			return "Telegram API Error " + errorCode + ": " + errorDesc;
	}
}
