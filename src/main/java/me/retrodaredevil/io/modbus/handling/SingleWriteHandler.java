package me.retrodaredevil.io.modbus.handling;

import me.retrodaredevil.io.modbus.ModbusMessage;
import me.retrodaredevil.io.modbus.ModbusMessages;

public class SingleWriteHandler implements MessageHandler<Void> {
	// https://www.modbustools.com/modbus.html#function06
	
	private final int register;
	private final int value;
	
	public SingleWriteHandler(int register, int value) {
		this.register = register;
		this.value = value;
	}
	
	@Override
	public ModbusMessage createMessage() {
		return ModbusMessages.createMessage(6, ModbusMessages.get8BitDataFrom16BitArray(register, value));
	}
	
	@Override
	public Void handleResponse(ModbusMessage response) {
		int functionCode = response.getFunctionCode();
		if(functionCode != 6){
			throw new FunctionCodeException(6, functionCode);
		}
		int[] data = ModbusMessages.get16BitDataFrom8BitArray(response.getData());
		if(data.length != 2){
			throw new ResponseLengthException(2, data.length);
		}
		// TODO figure out if we need to do a check for data[0]
		int setValue = data[1];
		if(setValue != value){
			throw new WriteValueException(value, setValue);
		}
		return null;
	}
}
