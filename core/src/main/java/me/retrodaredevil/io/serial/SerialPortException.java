package me.retrodaredevil.io.serial;

import org.jspecify.annotations.NullMarked;

import java.io.IOException;

@NullMarked
public class SerialPortException extends IOException {
	public SerialPortException() {
	}
	
	public SerialPortException(String message) {
		super(message);
	}
	
	public SerialPortException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public SerialPortException(Throwable cause) {
		super(cause);
	}
	
}
