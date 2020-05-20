package me.retrodaredevil.io.modbus;

/**
 * Represents the function code and data of a Modbus message.
 * <p>
 * This does not contain the checksum or the slave address, or MBAP header data.
 */
public interface ModbusMessage {
	/**
	 * @return The function code
	 */
	int getFunctionCode();
	byte getByteFunctionCode();

	/**
	 * NOTE: Do not modify the returned array. Doing so may produce undefined results
	 * @return An array where each element represents a single byte (8 bit number).
	 */
	int[] getData();
	/**
	 * NOTE: Do not modify the returned array. Doing so may produce undefined results
	 * @return An array where each element represents a single byte (8 bit number).
	 */
	byte[] getByteData();
}
