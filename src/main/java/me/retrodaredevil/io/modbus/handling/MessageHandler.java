package me.retrodaredevil.io.modbus.handling;

import me.retrodaredevil.io.modbus.ModbusMessage;

public interface MessageHandler<T> {
	ModbusMessage createMessage();
	T handleResponse(ModbusMessage response);
}
