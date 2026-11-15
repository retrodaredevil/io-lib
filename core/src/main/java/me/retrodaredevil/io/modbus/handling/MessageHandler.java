package me.retrodaredevil.io.modbus.handling;

import me.retrodaredevil.io.modbus.ModbusMessage;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface MessageHandler<T extends @Nullable Object> {
	ModbusMessage createRequest();

	/**
	 * @param response The response message
	 * @return The result parsed from the response
	 * @throws ParsedResponseException If the response is corrupt or a part of it is unexpected
	 */
	T handleResponse(ModbusMessage response);
}
