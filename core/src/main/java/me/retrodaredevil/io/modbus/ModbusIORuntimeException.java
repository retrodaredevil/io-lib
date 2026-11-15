package me.retrodaredevil.io.modbus;

import org.jspecify.annotations.NullMarked;

/**
 * Should be thrown when an {@link java.io.IOException} needs to be rethrown, or when
 * an IO related error has occurred
 */
@NullMarked
public class ModbusIORuntimeException extends ModbusRuntimeException {
	public ModbusIORuntimeException() {
	}

	public ModbusIORuntimeException(String message) {
		super(message);
	}

	public ModbusIORuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public ModbusIORuntimeException(Throwable cause) {
		super(cause);
	}

	public ModbusIORuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
