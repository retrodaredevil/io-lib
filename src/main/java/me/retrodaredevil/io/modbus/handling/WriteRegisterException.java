package me.retrodaredevil.io.modbus.handling;

public class WriteRegisterException extends WriteException {
	public WriteRegisterException(int expectedRegister, int actualRegister){
		this("Tried writing to register: " + expectedRegister + " but actually wrote to register: " + actualRegister);
	}
	public WriteRegisterException() {
	}
	
	public WriteRegisterException(String message) {
		super(message);
	}
	
	public WriteRegisterException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public WriteRegisterException(Throwable cause) {
		super(cause);
	}
	
	public WriteRegisterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
