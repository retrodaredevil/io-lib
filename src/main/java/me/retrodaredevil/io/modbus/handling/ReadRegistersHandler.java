package me.retrodaredevil.io.modbus.handling;

import me.retrodaredevil.io.modbus.FunctionCode;
import me.retrodaredevil.io.modbus.ModbusMessage;
import me.retrodaredevil.io.modbus.ModbusMessages;

import static me.retrodaredevil.io.modbus.ModbusMessages.get16BitDataFrom8BitArray;

public class ReadRegistersHandler implements MessageHandler<int[]> {
	
	private final int register;
	private final int numberOfRegisters;
	
	/**
	 *
	 * @param register The starting register
	 * @param numberOfRegisters The number of registers to read. The array returned from {@link #handleResponse(ModbusMessage)} will have a length of this
	 */
	public ReadRegistersHandler(int register, int numberOfRegisters) {
		this.register = register;
		this.numberOfRegisters = numberOfRegisters;
	}
	
	@Override
	public ModbusMessage createMessage() {
		return ModbusMessages.createMessage(FunctionCode.READ_REGISTERS, new int[] {
				(register & 0xFF00) >> 8, register & 0xFF,
				(numberOfRegisters & 0xFF00) >> 8, numberOfRegisters & 0xFF
		});
	}
	
	@Override
	public int[] handleResponse(ModbusMessage response) {
		int functionCode = response.getFunctionCode();
		if(functionCode != FunctionCode.READ_REGISTERS){
			throw new FunctionCodeException(FunctionCode.READ_REGISTERS, functionCode);
		}
		int[] allData = response.getData();
		int byteCount = allData[0];
		if(byteCount != numberOfRegisters * 2){
			throw new ResponseLengthException(numberOfRegisters * 2, byteCount);
		}
		if(allData.length != byteCount + 1){
			throw new ResponseLengthException(byteCount + 1, allData.length);
		}
		int[] data = new int[allData.length - 1];
		System.arraycopy(allData, 1, data, 0, data.length);
		return get16BitDataFrom8BitArray(data);
	}
}
