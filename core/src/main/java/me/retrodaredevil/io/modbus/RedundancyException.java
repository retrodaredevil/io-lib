package me.retrodaredevil.io.modbus;

public class RedundancyException extends ModbusRuntimeException {
	public RedundancyException(String redundancyType, int expected, int actual){
		this(redundancyType, expected, actual, "");
	}

	public RedundancyException(String redundancyType, int expected, int actual, String extra){
		this("Incorrect " + redundancyType + " checksum. Expected: " + expected + " but got: " + actual + (extra.isEmpty() ? "" : " " + extra));
	}
	
	public RedundancyException() {
	}
	
	public RedundancyException(String message) {
		super(message);
	}
	
	public RedundancyException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public RedundancyException(Throwable cause) {
		super(cause);
	}
	
	public RedundancyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
