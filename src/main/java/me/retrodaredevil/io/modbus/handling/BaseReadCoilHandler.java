package me.retrodaredevil.io.modbus.handling;

import me.retrodaredevil.io.modbus.ModbusMessage;
import me.retrodaredevil.io.modbus.ModbusMessages;
import me.retrodaredevil.io.modbus.ModbusRuntimeException;

import static me.retrodaredevil.io.modbus.ModbusMessages.get8BitDataFrom16BitArray;

public class BaseReadCoilHandler implements MessageHandler<boolean[]> {
	private final int functionCode;

	private final int dataAddress;
	private final int numberOfCoils;

	protected BaseReadCoilHandler(int functionCode, int dataAddress, int numberOfCoils) {
		this.functionCode = functionCode;
		this.dataAddress = dataAddress;
		this.numberOfCoils = numberOfCoils;
	}

	@Override
	public ModbusMessage createRequest() {
		return ModbusMessages.createMessage(functionCode, get8BitDataFrom16BitArray(dataAddress, numberOfCoils));
	}

	@Override
	public boolean[] handleResponse(ModbusMessage response) {
		if(response.getFunctionCode() != functionCode) {
			throw new FunctionCodeException(functionCode, response.getFunctionCode());
		}
		int[] data = response.getData();
		int expectedBytesToFollow = (numberOfCoils + 7) / 8; // ceil the result of numberOfCoils / 8
		int expectedLength = 1 + expectedBytesToFollow;
		if (expectedLength != data.length) {
			throw new ResponseLengthException(expectedLength, data.length);
		}
		int bytesToFollow = data[0];
		if (bytesToFollow != expectedBytesToFollow) {
			throw new ModbusRuntimeException("Expected " + expectedBytesToFollow + ", but it says " + bytesToFollow + " should follow. (Inconsistent)");
		}
		boolean[] r = new boolean[numberOfCoils];
		for (int i = 0; i < r.length; i++) {
			int byteIndex = i / 8;
			int position = i % 8;
			int byteValue = data[1 + byteIndex];
			r[i] = ((byteValue >> position) & 1) != 0;
		}
		return r;
	}
}
