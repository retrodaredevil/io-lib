package me.retrodaredevil.io.modbus.handling;

import me.retrodaredevil.io.modbus.ModbusMessage;

public interface MessageHandler<T> {
	ModbusMessage createRequest();

	/**
	 * @param response The response message
	 * @return The result parsed from the response
	 * @throws ResponseException If the response is corrupt or a part of it is unexpected
	 */
	T handleResponse(ModbusMessage response);
}
