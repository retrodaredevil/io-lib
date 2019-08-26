package me.retrodaredevil.io.modbus.handling;

import me.retrodaredevil.io.modbus.FunctionCode;
import me.retrodaredevil.io.modbus.ModbusMessage;
import me.retrodaredevil.io.modbus.ModbusMessages;

public class SingleWriteHandler implements MessageHandler<Void> {
	// https://www.modbustools.com/modbus.html#function06
	
	private final int register;
	private final int value;
	private final boolean checkRegister;
	
	public SingleWriteHandler(int register, int value, boolean checkRegister) {
		this.register = register;
		this.value = value;
		this.checkRegister = checkRegister;
	}
	public SingleWriteHandler(int register, int value) {
		this(register, value, true);
	}
	
	@Override
	public ModbusMessage createMessage() {
		return ModbusMessages.createMessage(FunctionCode.WRITE_SINGLE_REGISTER, ModbusMessages.get8BitDataFrom16BitArray(register, value));
	}
	
	@Override
	public Void handleResponse(ModbusMessage response) {
		int functionCode = response.getFunctionCode();
		if(functionCode != FunctionCode.WRITE_SINGLE_REGISTER){
			throw new FunctionCodeException(6, functionCode);
		}
		int[] data = ModbusMessages.get16BitDataFrom8BitArray(response.getData());
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
}
