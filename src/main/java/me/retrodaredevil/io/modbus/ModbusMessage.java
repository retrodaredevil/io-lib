package me.retrodaredevil.io.modbus;

public interface ModbusMessage {
	/**
	 * @return The function code
	 */
	int getFunctionCode();
	byte getByteFunctionCode();
	
	/**
	 * @return An array where each element represents a single byte (8 bit number).
	 */
	int[] getData();
	byte[] getByteData();
}
