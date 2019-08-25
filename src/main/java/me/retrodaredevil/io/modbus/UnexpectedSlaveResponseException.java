package me.retrodaredevil.io.modbus;

public class UnexpectedSlaveResponseException  extends ModbusRuntimeException {
	public UnexpectedSlaveResponseException(int expectedAddress, int slaveResponseAddress){
		this("Address: " + (expectedAddress & 0xFF) + " was expected but slave with address: " + (slaveResponseAddress & 0xFF) + " responded!");
	}
	public UnexpectedSlaveResponseException() {
	}
	
	public UnexpectedSlaveResponseException(String message) {
		super(message);
	}
	
	public UnexpectedSlaveResponseException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public UnexpectedSlaveResponseException(Throwable cause) {
		super(cause);
	}
	
	public UnexpectedSlaveResponseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
