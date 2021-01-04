package me.retrodaredevil.io.modbus.handling;

import me.retrodaredevil.io.modbus.FunctionCode;
import me.retrodaredevil.io.modbus.ModbusMessage;
import me.retrodaredevil.io.modbus.ModbusMessages;
import me.retrodaredevil.io.modbus.parsing.MessageParseException;

import java.util.Objects;

import static me.retrodaredevil.io.modbus.ModbusMessages.get16BitDataFrom8BitArray;
import static me.retrodaredevil.io.modbus.ModbusMessages.get8BitDataFrom16BitArray;

public class ReadHoldingRegisters extends BaseStartingDataAddress implements MessageResponseCreator<int[]> {
	private final int numberOfRegisters;

	/**
	 *
	 * @param startingDataAddress The starting register
	 * @param numberOfRegisters The number of registers to read. The array returned from {@link #handleResponse(ModbusMessage)} will have a length of this
	 */
	public ReadHoldingRegisters(int startingDataAddress, int numberOfRegisters) {
		super(startingDataAddress);
		this.numberOfRegisters = numberOfRegisters;
	}
	public static ReadHoldingRegisters parseFromRequestData(int[] data) throws MessageParseException {
		if (data.length != 4) {
			throw new MessageParseException("data.length != 4! data.length=" + data.length);
		}
		return new ReadHoldingRegisters(
				data[0] << 8 | data[1],
				data[2] << 8 | data[3]
		);
	}

	public int getNumberOfRegisters() {
		return numberOfRegisters;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ReadHoldingRegisters that = (ReadHoldingRegisters) o;
		return getStartingDataAddress() == that.getStartingDataAddress() &&
				getNumberOfRegisters() == that.getNumberOfRegisters();
	}

	@Override
	public int hashCode() {
		return Objects.hash(getStartingDataAddress(), getNumberOfRegisters());
	}

	@Override
	public ModbusMessage createRequest() {
		return ModbusMessages.createMessage(FunctionCode.READ_HOLDING_REGISTERS, get8BitDataFrom16BitArray(getStartingDataAddress(), numberOfRegisters));
	}

	@Override
	public int[] handleResponse(ModbusMessage response) {
		int expectedLength = numberOfRegisters * 2 + 1;
		HandleResponseHelper.checkResponse(response, FunctionCode.READ_HOLDING_REGISTERS, expectedLength);
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
		return ModbusMessages.createMessage(FunctionCode.READ_HOLDING_REGISTERS, allData);
	}
}
