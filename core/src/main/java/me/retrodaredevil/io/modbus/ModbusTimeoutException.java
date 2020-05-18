package me.retrodaredevil.io.modbus;

public class ModbusTimeoutException extends ModbusRuntimeException {
	public ModbusTimeoutException() {
	}
	
	public ModbusTimeoutException(String message) {
		super(message);
	}
	
	public ModbusTimeoutException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ModbusTimeoutException(Throwable cause) {
		super(cause);
	}
	
	public ModbusTimeoutException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
