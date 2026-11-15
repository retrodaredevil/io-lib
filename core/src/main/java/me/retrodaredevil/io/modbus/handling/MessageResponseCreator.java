package me.retrodaredevil.io.modbus.handling;

import me.retrodaredevil.io.modbus.ModbusMessage;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface MessageResponseCreator<T extends @Nullable Object> extends MessageHandler<T> {
	/**
	 * Creates a response message with the specified data
	 * @param data The data to put in the returned response message
	 * @return A response {@link ModbusMessage} with the specified data
	 * @throws IllegalArgumentException If {@code data} is not valid
	 */
	ModbusMessage createResponse(T data);
}
