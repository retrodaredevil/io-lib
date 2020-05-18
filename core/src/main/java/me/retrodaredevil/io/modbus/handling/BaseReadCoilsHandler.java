package me.retrodaredevil.io.modbus.handling;

import me.retrodaredevil.io.modbus.ModbusMessage;
import me.retrodaredevil.io.modbus.ModbusMessages;

import java.util.Objects;

import static me.retrodaredevil.io.modbus.ModbusMessages.get8BitDataFrom16BitArray;

public class BaseReadCoilsHandler extends BaseStartingDataAddress implements MessageResponseCreator<boolean[]> {
	private final int functionCode;

	private final int numberOfCoils;

	protected BaseReadCoilsHandler(int functionCode, int startingDataAddress, int numberOfCoils) {
		super(startingDataAddress);
		this.functionCode = functionCode;
		this.numberOfCoils = numberOfCoils;
	}
	public int getNumberOfCoils() {
		return numberOfCoils;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BaseReadCoilsHandler that = (BaseReadCoilsHandler) o;
		return functionCode == that.functionCode &&
				getStartingDataAddress() == that.getStartingDataAddress() &&
				getNumberOfCoils() == that.getNumberOfCoils();
	}

	@Override
	public int hashCode() {
		return Objects.hash(functionCode, getStartingDataAddress(), getNumberOfCoils());
	}

	@Override
	public ModbusMessage createRequest() {
		return ModbusMessages.createMessage(functionCode, get8BitDataFrom16BitArray(getStartingDataAddress(), numberOfCoils));
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
			throw new ResponseException("Expected " + expectedBytesToFollow + ", but it says " + bytesToFollow + " should follow. (Inconsistent)");
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

	@Override
	public ModbusMessage createResponse(boolean[] data) {
		if (numberOfCoils != data.length) {
			throw new IllegalArgumentException("data.length should be equal to number of coils! numberOfCoils=" + numberOfCoils);
		}
		int bytesToFollow = (numberOfCoils + 7) / 8;
		int[] responseData = new int[1 + bytesToFollow];
		responseData[0] = bytesToFollow;
		for (int i = 0; i < data.length; i++) {
			int byteIndex = i / 8;
			int position = i % 8;
			responseData[1 + byteIndex] |= 1 << position;
		}
		return ModbusMessages.createMessage(functionCode, responseData);
	}
}
