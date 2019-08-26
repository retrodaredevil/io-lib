package me.retrodaredevil.io.modbus.handling;

import me.retrodaredevil.io.modbus.FunctionCode;
import me.retrodaredevil.io.modbus.ModbusMessage;
import me.retrodaredevil.io.modbus.ModbusMessages;

public class MultipleWriteHandler implements MessageHandler<Void> {
	private final int register;
	private final int[] data8Bit;
	private final boolean checkRegister;
	public MultipleWriteHandler(int register, int[] data8Bit, boolean checkRegister){
		this.register = register;
		this.data8Bit = data8Bit;
		this.checkRegister = checkRegister;
		if(data8Bit.length % 2 != 0){
			throw new IllegalArgumentException("Length of data8Bit must be a multiple of two!");
		}
	}
	public MultipleWriteHandler(int register, int[] data8Bit){
		this(register, data8Bit, true);
	}
	@Override
	public ModbusMessage createMessage() {
		int[] data = new int[5 + data8Bit.length];
		data[0] = (register & 0xFF00) >> 8;
		data[1] = register & 0xFF;
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
		if(checkRegister) {
			int setRegister = (data[0] << 8) | data[1];
			if (setRegister != register) {
				throw new WriteRegisterException(register, setRegister);
			}
		}
		int setNumberOfRegisters = (data[2] << 8) | data[3];
		int expectedNumberOfRegisters = data8Bit.length / 2;
		if(setNumberOfRegisters != expectedNumberOfRegisters){
			throw new ResponseLengthException(expectedNumberOfRegisters, setNumberOfRegisters);
		}
		return null;
	}
}
