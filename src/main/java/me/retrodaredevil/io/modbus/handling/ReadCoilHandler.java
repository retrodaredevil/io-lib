package me.retrodaredevil.io.modbus.handling;

import me.retrodaredevil.io.modbus.FunctionCode;

public class ReadCoilHandler extends BaseReadCoilHandler {
	public ReadCoilHandler(int input, int numberOfCoils) {
		super(FunctionCode.READ_COIL, input, numberOfCoils);
	}
}
