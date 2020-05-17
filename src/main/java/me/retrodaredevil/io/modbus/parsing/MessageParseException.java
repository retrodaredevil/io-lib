package me.retrodaredevil.io.modbus.parsing;

public class MessageParseException extends Exception {
	public MessageParseException() {
	}

	public MessageParseException(String message) {
		super(message);
	}

	public MessageParseException(String message, Throwable cause) {
		super(message, cause);
	}

	public MessageParseException(Throwable cause) {
		super(cause);
	}

	public MessageParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
