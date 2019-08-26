package me.retrodaredevil.io.modbus.handling;

public class WriteValueException extends ResponseException {
	public WriteValueException(int expectedValue, int actualValue){
		this("We tried writing value: " + expectedValue + " but actually wrote value: " + actualValue);
	}
	public WriteValueException() {
	}
	
	public WriteValueException(String message) {
		super(message);
	}
	
	public WriteValueException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public WriteValueException(Throwable cause) {
		super(cause);
	}
	
	public WriteValueException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
