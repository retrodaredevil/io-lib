package me.retrodaredevil.io.modbus.handling;

import me.retrodaredevil.io.modbus.FunctionCode;
import me.retrodaredevil.io.modbus.ModbusMessage;
import me.retrodaredevil.io.modbus.ModbusMessages;
import me.retrodaredevil.io.modbus.parsing.MessageParseException;

import java.util.Arrays;
import java.util.Objects;

import static me.retrodaredevil.io.modbus.ModbusMessages.get16BitDataFrom8BitArray;

public class WriteSingleRegister extends BaseSingleWrite implements MessageResponseCreator<Void> {
	// https://www.modbustools.com/modbus.html#function06

	private final int value;

	/**
	 * @param dataAddress The data address of the register to write to
	 * @param value The value to write
	 */
	public WriteSingleRegister(int dataAddress, int value) {
		super(dataAddress);
		this.value = value;
	}
	public static WriteSingleRegister parseFromRequestData(int[] data) throws MessageParseException {
		if (data.length != 4) {
			throw new MessageParseException("data.length != 4! data.length=" + data.length);
		}
		int[] data16Bit = get16BitDataFrom8BitArray(data);
		return new WriteSingleRegister(data16Bit[0], data16Bit[1]);
	}

	/**
	 * @return The 16 bit value to write
	 */
	public int getValue() {
		return value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		WriteSingleRegister that = (WriteSingleRegister) o;
		return getDataAddress() == that.getDataAddress() &&
				getValue() == that.getValue();
	}

	@Override
	public int hashCode() {
		return Objects.hash(getDataAddress(), getValue());
	}

	@Override
	public ModbusMessage createRequest() {
		return ModbusMessages.createMessage(FunctionCode.WRITE_SINGLE_REGISTER, ModbusMessages.get8BitDataFrom16BitArray(getDataAddress(), value));
	}

	@Override
	public Void handleResponse(ModbusMessage response) {
		int functionCode = response.getFunctionCode();
		if(functionCode != FunctionCode.WRITE_SINGLE_REGISTER){
			throw new FunctionCodeException(FunctionCode.WRITE_SINGLE_REGISTER, functionCode);
		}
		int[] data8Bit = response.getData();
		if(data8Bit.length != 4){
			throw new ResponseLengthException(4, data8Bit.length);
		}
		int[] data = get16BitDataFrom8BitArray(data8Bit);
		int setRegister = data[0];
		checkDataAddress(setRegister);
		int setValue = data[1];
		if (setValue != value) {
			throw new WriteValueException(value, setValue);
		}
		return null;
	}

	@Override
	public ModbusMessage createResponse(Void data) {
		return ModbusMessages.createMessage(FunctionCode.WRITE_SINGLE_REGISTER, ModbusMessages.get8BitDataFrom16BitArray(getDataAddress(), value));
	}
}
