package me.retrodaredevil.io.modbus.handling;

import me.retrodaredevil.io.modbus.ModbusMessage;

public class ErrorCodeException extends FunctionCodeException {
	private final int exceptionCode;
	public ErrorCodeException(ModbusMessage response, int expectedFunctionCode, int exceptionCode) {
		super(response, expectedFunctionCode, "Expected to get " + expectedFunctionCode + " as the function code, but actually got " + response.getFunctionCode() + ". With exception code " + exceptionCode);
		this.exceptionCode = exceptionCode;
	}

	public int getExceptionCode() {
		return exceptionCode;
	}
}
