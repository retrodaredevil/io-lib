package me.retrodaredevil.io.modbus.handling;

import me.retrodaredevil.io.modbus.FunctionCode;
import me.retrodaredevil.io.modbus.parsing.MessageParseException;

import static me.retrodaredevil.io.modbus.ModbusMessages.get16BitDataFrom8BitArray;

public class ReadDiscreteInputs extends BaseReadCoilsHandler {
	public ReadDiscreteInputs(int input, int numberOfCoils) {
		super(FunctionCode.READ_DISCRETE_INPUT, input, numberOfCoils);
	}

	public static ReadDiscreteInputs parseFromRequestData(int[] data) throws MessageParseException {
		if (data.length != 4) {
			throw new MessageParseException("data.length != 4! data.length=" + data.length);
		}
		int[] data16Bit = get16BitDataFrom8BitArray(data);
		return new ReadDiscreteInputs(data16Bit[0], data16Bit[1]);
	}
}
