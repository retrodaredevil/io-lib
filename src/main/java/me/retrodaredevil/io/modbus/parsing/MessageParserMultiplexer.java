package me.retrodaredevil.io.modbus.parsing;

import me.retrodaredevil.io.modbus.ModbusMessage;
import me.retrodaredevil.io.modbus.handling.MessageHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MessageParserMultiplexer implements MessageParser {
	private final List<MessageParser> messageParsers;

	public MessageParserMultiplexer(Collection<? extends MessageParser> messageParsers) {
		this.messageParsers = Collections.unmodifiableList(new ArrayList<>(messageParsers));
	}

	@Override
	public MessageHandler<?> parseRequestMessage(ModbusMessage message) throws MessageParseException {
		for (MessageParser messageParser : messageParsers) {
			MessageHandler<?> messageHandler = messageParser.parseRequestMessage(message);
			if (messageHandler != null) {
				return messageHandler;
			}
		}
		return null;
	}
}
