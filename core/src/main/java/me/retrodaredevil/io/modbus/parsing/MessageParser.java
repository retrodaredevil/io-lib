package me.retrodaredevil.io.modbus.parsing;

import me.retrodaredevil.io.modbus.ModbusMessage;
import me.retrodaredevil.io.modbus.handling.MessageHandler;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface MessageParser {
	/**
	 * @param message The message from the master
	 * @return The {@link MessageHandler} if the function is supported, null otherwise
	 * @throws MessageParseException if the data in {@code message} is not valid.
	 */
	@Nullable MessageHandler<?> parseRequestMessage(ModbusMessage message) throws MessageParseException;
}
