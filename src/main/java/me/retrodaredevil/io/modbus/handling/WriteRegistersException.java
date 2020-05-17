package me.retrodaredevil.io.modbus.handling;

public class WriteRegistersException extends WriteException {
	public WriteRegistersException() {
	}

	public WriteRegistersException(String message) {
		super(message);
	}

	public WriteRegistersException(String message, Throwable cause) {
		super(message, cause);
	}

	public WriteRegistersException(Throwable cause) {
		super(cause);
	}

	public WriteRegistersException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
