package me.retrodaredevil.io.modbus.handling;

import me.retrodaredevil.io.modbus.FunctionCode;
import me.retrodaredevil.io.modbus.parsing.MessageParseException;

public class ReadInputRegisters extends BaseReadRegisters {
	public ReadInputRegisters(int startingDataAddress, int numberOfRegisters) {
		super(startingDataAddress, FunctionCode.READ_HOLDING_REGISTERS, numberOfRegisters);
	}
	public static ReadInputRegisters parseFromRequestData(int[] data) throws MessageParseException {
		if (data.length != 4) {
			throw new MessageParseException("data.length != 4! data.length=" + data.length);
		}
		return new ReadInputRegisters(
				data[0] << 8 | data[1],
				data[2] << 8 | data[3]
		);
	}

}

