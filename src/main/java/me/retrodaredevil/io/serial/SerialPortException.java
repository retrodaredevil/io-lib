package me.retrodaredevil.io.serial;

import java.io.IOException;

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
