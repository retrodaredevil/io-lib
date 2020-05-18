package me.retrodaredevil.io.modbus.handling;

import me.retrodaredevil.io.modbus.FunctionCode;
import me.retrodaredevil.io.modbus.ModbusMessage;
import me.retrodaredevil.io.modbus.ModbusMessages;
import me.retrodaredevil.io.modbus.parsing.MessageParseException;

import java.util.Arrays;
import java.util.Objects;

import static me.retrodaredevil.io.modbus.ModbusMessages.*;

public class WriteMultipleRegisters extends BaseStartingDataAddress implements MessageResponseCreator<Void> {
	private final int[] data8Bit;

	/**
	 * @param startingDataAddress The data address of the starting register
	 * @param data8Bit The 8 bit data to write
	 */
	public WriteMultipleRegisters(int startingDataAddress, int[] data8Bit){
		super(startingDataAddress);
		this.data8Bit = data8Bit;
		if(data8Bit.length % 2 != 0){
			throw new IllegalArgumentException("Length of data8Bit must be a multiple of two!");
		}
	}
	public WriteMultipleRegisters(int dataAddress, byte[] data8Bit) {
		this(dataAddress, convert8BitArray(data8Bit));
	}
	public static WriteMultipleRegisters parseFromRequestData(int[] data) throws MessageParseException {
		if (data.length % 2 != 1) { // the array's length is not odd // if it is even
			throw new MessageParseException("data.length is even! It must be odd! data.length=" + data.length);
		}
		if (data.length <= 5) {
			throw new MessageParseException("data.length must be greater than 5 and must be odd! So must be >= 7. data.length=" + data.length);
		}
		int dataAddress = data[0] << 8 | data[1];
		int numberOfRegisters = data[2] << 8 | data[3];
		int numberOfBytes = data[4];
		if (numberOfBytes != numberOfRegisters * 2) {
			throw new MessageParseException("numberOfBytes=" + numberOfBytes + " and numberOfRegisters=" + numberOfRegisters + "! numberOfBytes must equal numberOfRegisters * 2!");
		}
		if (data.length - 5 != numberOfBytes) {
			throw new MessageParseException("data.length - 5 must equal numberOfBytes! data.length=" + data.length + " numberOfBytes=" + numberOfBytes);
		}
		int[] data8Bit = Arrays.copyOfRange(data, 5, data.length);
		return new WriteMultipleRegisters(dataAddress, data8Bit);
	}

	/**
	 * @return The 8 bit data array of values to write. The length is always a multiple of 2
	 */
	public int[] getData8Bit() {
		return data8Bit;
	}
	/**
	 * @return The 16 bit data array of values to write.
	 */
	public int[] getData16Bit() {
		return get16BitDataFrom8BitArray(data8Bit);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		WriteMultipleRegisters that = (WriteMultipleRegisters) o;
		return getStartingDataAddress() == that.getStartingDataAddress() &&
				Arrays.equals(getData8Bit(), that.getData8Bit());
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(getStartingDataAddress());
		result = 31 * result + Arrays.hashCode(getData8Bit());
		return result;
	}

	@Override
	public ModbusMessage createRequest() {
		int[] data = new int[5 + data8Bit.length];
		data[0] = getStartingDataAddressHigh();
		data[1] = getStartingDataAddressLow();
		int numberOfRegisters = data8Bit.length / 2;
		data[2] = (numberOfRegisters & 0xFF00) >> 8; // really this should always be zero
		data[3] = numberOfRegisters & 0xFF; // this should always be half of the number of bytes
		data[4] = data8Bit.length; // number of bytes
		System.arraycopy(data8Bit, 0, data, 5, data8Bit.length);
		return ModbusMessages.createMessage(FunctionCode.WRITE_MULTIPLE_REGISTERS, data);
	}

	@Override
	public Void handleResponse(ModbusMessage response) {
		int functionCode = response.getFunctionCode();
		if(functionCode != FunctionCode.WRITE_MULTIPLE_REGISTERS){
			throw new FunctionCodeException(FunctionCode.WRITE_MULTIPLE_REGISTERS, functionCode);
		}
		int[] data = response.getData();
		if (data.length != 4) {
			throw new ResponseLengthException("Expected a length of 4! Got" + data.length + " instead. data: " + Arrays.toString(data));
		}
		int setRegister = (data[0] << 8) | data[1];
		if (setRegister != getStartingDataAddress()) {
			throw new WriteException("Tried writing to starting address: " + getStartingDataAddress() + " but actually wrote to " + setRegister);
		}
		int setNumberOfRegisters = (data[2] << 8) | data[3];
		int expectedNumberOfRegisters = data8Bit.length / 2;
		if(setNumberOfRegisters != expectedNumberOfRegisters){
			throw new WriteException("Tried writing to " + expectedNumberOfRegisters + " registers but actually wrote to " + setNumberOfRegisters + " registers. Start register was correct: " + getStartingDataAddress());
		}
		return null;
	}

	@Override
	public ModbusMessage createResponse(Void data) {
		int numberOfRegisters = data8Bit.length / 2;
		return ModbusMessages.createMessage(FunctionCode.WRITE_MULTIPLE_REGISTERS, get8BitDataFrom16BitArray(getStartingDataAddress(), numberOfRegisters));
	}
}
