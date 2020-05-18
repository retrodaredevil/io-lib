package me.retrodaredevil.io.modbus.handling;

import me.retrodaredevil.io.modbus.FunctionCode;
import me.retrodaredevil.io.modbus.parsing.MessageParseException;

import static me.retrodaredevil.io.modbus.ModbusMessages.get16BitDataFrom8BitArray;

public class ReadCoils extends BaseReadCoilsHandler {
	public ReadCoils(int input, int numberOfCoils) {
		super(FunctionCode.READ_COIL, input, numberOfCoils);
	}

	public static ReadCoils parseFromRequestData(int[] data) throws MessageParseException {
		if (data.length != 4) {
			throw new MessageParseException("data.length != 4! data.length=" + data.length);
		}
		int[] data16Bit = get16BitDataFrom8BitArray(data);
		return new ReadCoils(data16Bit[0], data16Bit[1]);
	}
}
