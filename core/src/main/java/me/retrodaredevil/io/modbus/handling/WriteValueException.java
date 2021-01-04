package me.retrodaredevil.io.modbus.handling;

import me.retrodaredevil.io.modbus.ModbusMessage;

public class WriteValueException extends WriteException {
	public WriteValueException(ModbusMessage response, int expectedValue, int actualValue){
		this(response, "We tried writing value: " + expectedValue + " but actually wrote value: " + actualValue);
	}
	public WriteValueException(ModbusMessage response, String message) {
		super(response, message);
	}
}
