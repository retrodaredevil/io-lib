package me.retrodaredevil.io.modbus.handling;

import me.retrodaredevil.io.modbus.FunctionCode;
import me.retrodaredevil.io.modbus.ModbusMessage;
import me.retrodaredevil.io.modbus.ModbusMessages;
import me.retrodaredevil.io.modbus.parsing.MessageParseException;

import java.util.Arrays;
import java.util.Objects;

import static me.retrodaredevil.io.modbus.ModbusMessages.get16BitDataFrom8BitArray;
import static me.retrodaredevil.io.modbus.ModbusMessages.get8BitDataFrom16BitArray;

public class WriteMultipleCoils extends BaseStartingDataAddress implements MessageResponseCreator<Void> {
	private final boolean[] coils;

	public WriteMultipleCoils(int startingDataAddress, boolean[] coils) {
		super(startingDataAddress);
		this.coils = coils;
	}
	public boolean[] getCoils() {
		return coils;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		WriteMultipleCoils that = (WriteMultipleCoils) o;
		return getStartingDataAddress() == that.getStartingDataAddress() &&
				Arrays.equals(getCoils(), that.getCoils());
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(getStartingDataAddress());
		result = 31 * result + Arrays.hashCode(getCoils());
		return result;
	}

	public static WriteMultipleCoils parseFromRequestData(int[] data) throws MessageParseException {
		if (data.length <= 5) { // we don't support a length of 5 because who would write no coils?
			throw new MessageParseException("data.length must be greater than 5! data.length=" + data.length);
		}
		int dataAddress = (data[0] << 8) | data[1];
		int numberOfCoils = (data[2] << 8) | data[3];
		int statedNumberOfBytes = data[4];
		int expectedNumberOfBytes = (numberOfCoils + 7) / 8;
		if (statedNumberOfBytes != expectedNumberOfBytes) {
			throw new MessageParseException("statedNumberOfBytes != expectedNumberOfBytes! statedNumberOfBytes=" + statedNumberOfBytes + " expectedNumberOfBytes=" + expectedNumberOfBytes);
		}
		int actualNumberOfBytes = data.length - 5;
		if (actualNumberOfBytes != statedNumberOfBytes) {
			throw new MessageParseException("actualNumberOfBytes != statedNumberOfBytes! actualNumberOfBytes=" + actualNumberOfBytes + " statedNumberOfBytes=" + statedNumberOfBytes);
		}
		boolean[] coils = new boolean[numberOfCoils];
		for (int i = 0; i < numberOfCoils; i++) {
			int byteIndex = i / 8;
			int position = i % 8;
			int byteValue = data[5 + byteIndex];
			coils[i] = ((byteValue >> position) & 1) != 0;
		}
		return new WriteMultipleCoils(dataAddress, coils);
	}

	@Override
	public ModbusMessage createRequest() {
		int numberOfBytes = (coils.length + 7) / 8;
		int[] data = new int[5 + numberOfBytes];
		data[0] = getStartingDataAddressHigh();
		data[1] = getStartingDataAddressLow();

		data[2] = coils.length >> 8;
		data[3] = coils.length & 0xFF;

		data[4] = numberOfBytes;

		for (int i = 0; i < coils.length; i++) {
			boolean value = coils[i];
			if (value) {
				int byteIndex = i / 8;
				int position = i % 8;
				data[5 + byteIndex] |= 1 << position;
			}
		}
		return ModbusMessages.createMessage(FunctionCode.WRITE_MULTIPLE_COILS, data);
	}

	@Override
	public Void handleResponse(ModbusMessage response) {
		HandleResponseHelper.checkResponse(response, FunctionCode.WRITE_MULTIPLE_COILS, 4);
		int[] data16Bit = get16BitDataFrom8BitArray(response.getData());
		int receivedDataAddress = data16Bit[0];
		int receivedNumberOfCoils = data16Bit[1];

		if (receivedDataAddress != getStartingDataAddress()) {
			throw new WriteException(response, "receivedDataAddress didn't match dataAddress. receivedDataAddress=" + receivedDataAddress + " dataAddress=" + getStartingDataAddress());
		}
		if (receivedNumberOfCoils != coils.length) {
			throw new WriteException(response, "receivedNumberOfCoils didn't match coils.length. receivedNumberOfCoils=" + receivedNumberOfCoils + " coils.length=" + coils.length);
		}
		return null;
	}

	@Override
	public ModbusMessage createResponse(Void data) {
		return ModbusMessages.createMessage(FunctionCode.WRITE_MULTIPLE_COILS, get8BitDataFrom16BitArray(getStartingDataAddress(), coils.length));
	}
}
