package me.retrodaredevil.io.modbus.handling;

import me.retrodaredevil.io.modbus.ModbusMessage;
import me.retrodaredevil.io.modbus.ModbusMessages;

import java.util.Objects;

import static me.retrodaredevil.io.modbus.ModbusMessages.get16BitDataFrom8BitArray;
import static me.retrodaredevil.io.modbus.ModbusMessages.get8BitDataFrom16BitArray;

public class BaseReadRegisters extends BaseStartingDataAddress implements MessageResponseCreator<int[]> {
	private final int functionCode;
	private final int numberOfRegisters;

	/**
	 * @param startingDataAddress The starting register
	 * @param functionCode
	 * @param numberOfRegisters The number of registers to read. The array returned from {@link #handleResponse(ModbusMessage)} will have a length of this
	 */
	public BaseReadRegisters(int startingDataAddress, int functionCode, int numberOfRegisters) {
		super(startingDataAddress);
		this.functionCode = functionCode;
		this.numberOfRegisters = numberOfRegisters;
	}

	public int getNumberOfRegisters() {
		return numberOfRegisters;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BaseReadRegisters that = (BaseReadRegisters) o;
		return getStartingDataAddress() == that.getStartingDataAddress() &&
				getNumberOfRegisters() == that.getNumberOfRegisters();
	}

	@Override
	public int hashCode() {
		return Objects.hash(getStartingDataAddress(), getNumberOfRegisters());
	}

	@Override
	public ModbusMessage createRequest() {
		return ModbusMessages.createMessage(functionCode, get8BitDataFrom16BitArray(getStartingDataAddress(), numberOfRegisters));
	}

	@Override
	public int[] handleResponse(ModbusMessage response) {
		int expectedLength = numberOfRegisters * 2 + 1;
		HandleResponseHelper.checkResponse(response, functionCode, expectedLength);
		int[] allData = response.getData();
		int byteCount = allData[0];
		if(byteCount != numberOfRegisters * 2){
			throw new ParsedResponseException(response, "Inconsistent byte count! byteCount=" + byteCount + ". expected=" + (numberOfRegisters * 2));
		}

		int[] data = new int[allData.length - 1];
		System.arraycopy(allData, 1, data, 0, data.length);
		return get16BitDataFrom8BitArray(data);
	}

	@Override
	public ModbusMessage createResponse(int[] data16Bit) {
		if (data16Bit.length != numberOfRegisters) {
			throw new IllegalArgumentException("The array of 16 bit integers passed was not the correct length! numberOfRegisters=" + numberOfRegisters + ". The passed data should have that length.");
		}
		int[] data = get8BitDataFrom16BitArray(data16Bit);
		int byteCount = numberOfRegisters * 2;
		if (data.length != byteCount) {
			throw new AssertionError("We just checked this. This is a code problem.");
		}
		int[] allData = new int[data.length + 1];
		allData[0] = byteCount;
		System.arraycopy(data, 0, allData, 1, data.length);
		return ModbusMessages.createMessage(functionCode, allData);
	}
}
