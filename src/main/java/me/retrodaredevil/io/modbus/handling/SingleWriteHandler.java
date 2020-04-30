package me.retrodaredevil.io.modbus.handling;

import me.retrodaredevil.io.modbus.FunctionCode;
import me.retrodaredevil.io.modbus.ModbusMessage;
import me.retrodaredevil.io.modbus.ModbusMessages;
import me.retrodaredevil.io.modbus.parsing.MessageParseException;

import static me.retrodaredevil.io.modbus.ModbusMessages.get16BitDataFrom8BitArray;

public class SingleWriteHandler implements MessageResponseCreator<Void> {
	// https://www.modbustools.com/modbus.html#function06
	
	private final int register;
	private final int value;
	private final boolean checkRegister;

	@Deprecated
	public SingleWriteHandler(int register, int value, boolean checkRegister) {
		this.register = register;
		this.value = value;
		this.checkRegister = checkRegister;
	}
	public SingleWriteHandler(int register, int value) {
		this(register, value, true);
	}
	public static SingleWriteHandler parseFromRequestData(int[] data) throws MessageParseException {
		if (data.length != 4) {
			throw new MessageParseException("data.length != 4! data.length=" + data.length);
		}
		int[] data16Bit = get16BitDataFrom8BitArray(data);
		return new SingleWriteHandler(data16Bit[0], data16Bit[1]);
	}

	/**
	 * @return The register to write to
	 */
	public int getRegister() {
		return register;
	}
	/**
	 * @return The 16 bit value to write
	 */
	public int getValue() {
		return value;
	}

	@Override
	public ModbusMessage createRequest() {
		return ModbusMessages.createMessage(FunctionCode.WRITE_SINGLE_REGISTER, ModbusMessages.get8BitDataFrom16BitArray(register, value));
	}
	
	@Override
	public Void handleResponse(ModbusMessage response) {
		int functionCode = response.getFunctionCode();
		if(functionCode != FunctionCode.WRITE_SINGLE_REGISTER){
			throw new FunctionCodeException(FunctionCode.WRITE_SINGLE_REGISTER, functionCode);
		}
		int[] data = get16BitDataFrom8BitArray(response.getData());
		if(data.length != 2){
			throw new ResponseLengthException(2, data.length);
		}
		if(checkRegister) {
			int setRegister = data[0];
			if (setRegister != register) {
				throw new WriteRegisterException(register, setRegister);
			}
		}
		int setValue = data[1];
		if (setValue != value) {
			throw new WriteValueException(value, setValue);
		}
		return null;
	}

	@Override
	public ModbusMessage createResponse(Void data) {
		int[] data16Bit = new int[] {
				register,
				value
		};
		return ModbusMessages.createMessage(FunctionCode.WRITE_SINGLE_REGISTER, ModbusMessages.get8BitDataFrom16BitArray(data16Bit));
	}
}
