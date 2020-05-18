package me.retrodaredevil.io.modbus.handling;

import me.retrodaredevil.io.modbus.ModbusRuntimeException;

public class ResponseException extends ModbusRuntimeException {
	public ResponseException() {
	}
	
	public ResponseException(String message) {
		super(message);
	}
	
	public ResponseException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ResponseException(Throwable cause) {
		super(cause);
	}
	
	public ResponseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
