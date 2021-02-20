package me.retrodaredevil.io.modbus.parsing;

import me.retrodaredevil.io.modbus.FunctionCode;
import me.retrodaredevil.io.modbus.ModbusMessage;
import me.retrodaredevil.io.modbus.handling.*;

public class DefaultMessageParser implements MessageParser {
	@Override
	public MessageHandler<?> parseRequestMessage(ModbusMessage message) throws MessageParseException {
		switch(message.getFunctionCode()) {
			case FunctionCode.READ_COIL:
				return ReadCoils.parseFromRequestData(message.getData());
			case FunctionCode.READ_DISCRETE_INPUT:
				return ReadDiscreteInputs.parseFromRequestData(message.getData());
			case FunctionCode.READ_HOLDING_REGISTERS:
				return ReadHoldingRegisters.parseFromRequestData(message.getData());
			case FunctionCode.READ_INPUT_REGISTERS:
				return ReadInputRegisters.parseFromRequestData(message.getData());
			case FunctionCode.WRITE_SINGLE_COIL:
				return WriteSingleCoil.parseFromRequestData(message.getData());
			case FunctionCode.WRITE_SINGLE_REGISTER:
				return WriteSingleRegister.parseFromRequestData(message.getData());
			case FunctionCode.WRITE_MULTIPLE_COILS:
				return WriteMultipleCoils.parseFromRequestData(message.getData());
			case FunctionCode.WRITE_MULTIPLE_REGISTERS:
				return WriteMultipleRegisters.parseFromRequestData(message.getData());
		}
		return null;
	}
}
