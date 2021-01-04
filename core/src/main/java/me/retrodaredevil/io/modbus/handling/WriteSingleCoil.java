package me.retrodaredevil.io.modbus.handling;

import me.retrodaredevil.io.modbus.FunctionCode;
import me.retrodaredevil.io.modbus.ModbusMessage;
import me.retrodaredevil.io.modbus.ModbusMessages;
import me.retrodaredevil.io.modbus.parsing.MessageParseException;

import java.util.Objects;

import static me.retrodaredevil.io.modbus.ModbusMessages.get16BitDataFrom8BitArray;
import static me.retrodaredevil.io.modbus.ModbusMessages.get8BitDataFrom16BitArray;

public class WriteSingleCoil extends BaseSingleWrite implements MessageResponseCreator<Void> {
	private final boolean value;

	public WriteSingleCoil(int dataAddress, boolean value) {
		super(dataAddress);
		this.value = value;
	}
	public static WriteSingleCoil parseFromRequestData(int[] data) throws MessageParseException {
		if (data.length != 4) {
			throw new MessageParseException("data.length != 4! data.length=" + data.length);
		}
		int[] data16Bit = get16BitDataFrom8BitArray(data);
		int valueEncoded = data16Bit[1];
		final boolean value;
		if (valueEncoded == 0xFF00) {
			value = true;
		} else if (valueEncoded == 0x0000) {
			value = false;
		} else {
			throw new MessageParseException("Unexpected encoded value: " + valueEncoded);
		}
		return new WriteSingleCoil(data16Bit[0], value);
	}
	public boolean getValue() {
		return value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		WriteSingleCoil that = (WriteSingleCoil) o;
		return getDataAddress() == that.getDataAddress() &&
				getValue() == that.getValue();
	}

	@Override
	public int hashCode() {
		return Objects.hash(getDataAddress(), getValue());
	}

	@Override
	public ModbusMessage createRequest() {
		return ModbusMessages.createMessage(FunctionCode.WRITE_SINGLE_COIL, get8BitDataFrom16BitArray(getDataAddress(), value ? 0xFF00 : 0x0000));
	}

	@Override
	public Void handleResponse(ModbusMessage response) {
		HandleResponseHelper.checkResponse(response, FunctionCode.WRITE_SINGLE_COIL, 4);
		int[] data8Bit = response.getData(); // we already checked that the length should be 4
		int[] data16Bit = get16BitDataFrom8BitArray(data8Bit);
		int receivedDataAddress = data16Bit[0];
		int writtenValue = data16Bit[1];
		checkDataAddress(response, receivedDataAddress);
		if (writtenValue == 0xFF00) {
			if (!value) {
				throw new WriteValueException(response, "We wanted to set value to false, but it stayed true.");
			}
		} else if (writtenValue == 0x0000) {
			if (value) {
				throw new WriteValueException(response, "We wanted to set value to true, but it stayed false.");
			}
		} else {
			throw new ParsedResponseException(response, "Unexpected written value: " + writtenValue);
		}
		return null;
	}

	@Override
	public ModbusMessage createResponse(Void data) {
		return ModbusMessages.createMessage(FunctionCode.WRITE_SINGLE_COIL, get8BitDataFrom16BitArray(getDataAddress(), value ? 0xFF00 : 0x0000));
	}
}
