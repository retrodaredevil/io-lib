package me.retrodaredevil.io.modbus.parsing;

import me.retrodaredevil.io.modbus.ModbusMessage;
import me.retrodaredevil.io.modbus.handling.MessageHandler;

public interface MessageParser {
	/**
	 * @param message The message from the master
	 * @return The {@link MessageHandler} if the function is supported, null otherwise
	 * @throws MessageParseException if the data in {@code message} is not valid.
	 */
	MessageHandler<?> parseRequestMessage(ModbusMessage message) throws MessageParseException;
}
