package me.retrodaredevil.io.modbus.handling;

import me.retrodaredevil.io.modbus.ModbusMessage;

public class WriteException extends ParsedResponseException {
	public WriteException(ModbusMessage response, String message) {
		super(response, message);
	}
}
