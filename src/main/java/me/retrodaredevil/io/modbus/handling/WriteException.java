package me.retrodaredevil.io.modbus.handling;

public class WriteException extends ResponseException {
	public WriteException() {
	}
	
	public WriteException(String message) {
		super(message);
	}
	
	public WriteException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public WriteException(Throwable cause) {
		super(cause);
	}
	
	public WriteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
