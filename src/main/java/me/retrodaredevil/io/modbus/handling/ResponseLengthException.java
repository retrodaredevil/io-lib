package me.retrodaredevil.io.modbus.handling;

public class ResponseLengthException extends ResponseException {
	public ResponseLengthException(int expectedLength, int actualLength){
		this("Expected a length of " + expectedLength + " but got a length of " + actualLength);
	}
	public ResponseLengthException() {
	}
	
	public ResponseLengthException(String message) {
		super(message);
	}
	
	public ResponseLengthException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ResponseLengthException(Throwable cause) {
		super(cause);
	}
	
	public ResponseLengthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
