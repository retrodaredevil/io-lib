package me.retrodaredevil.io.modbus.handling;

import me.retrodaredevil.io.modbus.ModbusMessage;
import me.retrodaredevil.io.modbus.ModbusRuntimeException;

public class ParsedResponseException extends ModbusRuntimeException {
	private final ModbusMessage response;
	public ParsedResponseException(ModbusMessage response, String message) {
		super(message);
		this.response = response;
	}

	public ModbusMessage getResponse() {
		return response;
	}
}
