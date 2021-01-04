package me.retrodaredevil.io.modbus.handling;

import me.retrodaredevil.io.modbus.ModbusMessage;

public class ParsedResponseDataLengthException extends ParsedResponseException {
	private final int expectedDataLength;
	public ParsedResponseDataLengthException(ModbusMessage response, int expectedDataLength) {
		super(response, "Expected a length of " + expectedDataLength + ". Got a length of " + response.getData().length + " instead!");
		this.expectedDataLength = expectedDataLength;
	}

	public int getExpectedDataLength() {
		return expectedDataLength;
	}
}
