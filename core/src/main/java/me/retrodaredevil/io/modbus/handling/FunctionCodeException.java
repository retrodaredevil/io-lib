package me.retrodaredevil.io.modbus.handling;

import me.retrodaredevil.io.modbus.ModbusMessage;

public class FunctionCodeException extends ParsedResponseException {
	private final int expectedFunctionCode;
	public FunctionCodeException(ModbusMessage response, int expectedFunctionCode){
		this(response, expectedFunctionCode, "Expected to get " + expectedFunctionCode + " as the function code, but actually got " + response.getFunctionCode());
	}
	public FunctionCodeException(ModbusMessage response, int expectedFunctionCode, String message){
		super(response, message);
		this.expectedFunctionCode = expectedFunctionCode;
	}

	public int getExpectedFunctionCode() {
		return expectedFunctionCode;
	}

	public int getActualFunctionCode() {
		return getResponse().getFunctionCode();
	}
}
