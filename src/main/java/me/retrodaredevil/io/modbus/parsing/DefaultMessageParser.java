package me.retrodaredevil.io.modbus.parsing;

import me.retrodaredevil.io.modbus.FunctionCode;
import me.retrodaredevil.io.modbus.ModbusMessage;
import me.retrodaredevil.io.modbus.handling.MessageHandler;
import me.retrodaredevil.io.modbus.handling.MultipleWriteHandler;
import me.retrodaredevil.io.modbus.handling.ReadRegistersHandler;
import me.retrodaredevil.io.modbus.handling.SingleWriteHandler;

public class DefaultMessageParser implements MessageParser {
	@Override
	public MessageHandler<?> parseRequestMessage(ModbusMessage message) throws MessageParseException {
		switch(message.getFunctionCode()) {
			case FunctionCode.READ_REGISTERS:
				return ReadRegistersHandler.parseFromRequestData(message.getData());
			case FunctionCode.WRITE_SINGLE_REGISTER:
				return SingleWriteHandler.parseFromRequestData(message.getData());
			case FunctionCode.WRITE_MULTIPLE_REGISTERS:
				return MultipleWriteHandler.parseFromRequestData(message.getData());
		}
		return null;
	}
}
