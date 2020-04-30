package me.retrodaredevil.io.modbus.handling;

import me.retrodaredevil.io.modbus.FunctionCode;
import me.retrodaredevil.io.modbus.ModbusMessage;
import me.retrodaredevil.io.modbus.ModbusMessages;
import me.retrodaredevil.io.modbus.parsing.MessageParseException;

import static me.retrodaredevil.io.modbus.ModbusMessages.get16BitDataFrom8BitArray;
import static me.retrodaredevil.io.modbus.ModbusMessages.get8BitDataFrom16BitArray;

public class ReadRegistersHandler implements MessageResponseCreator<int[]> {
	
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
	public static ReadRegistersHandler parseFromRequestData(int[] data) throws MessageParseException {
	    if (data.length != 4) {
	    	throw new MessageParseException("data.length != 4! data.length=" + data.length);
		}
		return new ReadRegistersHandler(
				data[0] << 8 | data[1],
				data[2] << 8 | data[3]
		);
	}

	public int getRegister() {
		return register;
	}
	public int getNumberOfRegisters() {
		return numberOfRegisters;
	}

	@Override
	public ModbusMessage createRequest() {
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
		return ModbusMessages.createMessage(FunctionCode.READ_REGISTERS, allData);
	}
}
