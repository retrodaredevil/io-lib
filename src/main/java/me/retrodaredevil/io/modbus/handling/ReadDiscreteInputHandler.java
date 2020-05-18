package me.retrodaredevil.io.modbus.handling;

import me.retrodaredevil.io.modbus.FunctionCode;

public class ReadDiscreteInputHandler extends BaseReadCoilHandler {
	public ReadDiscreteInputHandler(int input, int numberOfCoils) {
		super(FunctionCode.READ_DISCRETE_INPUT, input, numberOfCoils);
	}
}
