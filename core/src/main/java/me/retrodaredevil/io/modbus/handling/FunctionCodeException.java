package me.retrodaredevil.io.modbus.handling;

public class FunctionCodeException extends ResponseException {
	public FunctionCodeException(int expectedFunctionCode, int actualFunctionCode){
		this("Expected to get " + expectedFunctionCode + " as the function code, but actually got " + actualFunctionCode);
	}
	public FunctionCodeException() {
	}
	
	public FunctionCodeException(String message) {
		super(message);
	}
	
	public FunctionCodeException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public FunctionCodeException(Throwable cause) {
		super(cause);
	}
	
	public FunctionCodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
