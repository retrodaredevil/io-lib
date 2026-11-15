package me.retrodaredevil.io.modbus.handling;

import me.retrodaredevil.io.modbus.ModbusMessage;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class WriteException extends ParsedResponseException {
	public WriteException(ModbusMessage response, String message) {
		super(response, message);
	}
}
