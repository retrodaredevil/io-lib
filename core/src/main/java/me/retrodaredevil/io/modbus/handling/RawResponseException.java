package me.retrodaredevil.io.modbus.handling;

import me.retrodaredevil.io.modbus.InvalidResponseException;
import me.retrodaredevil.io.modbus.ModbusRuntimeException;

public class RawResponseException extends InvalidResponseException {
	private final byte[] rawData;
	public RawResponseException(byte[] rawData, String message) {
		super(message);
		this.rawData = rawData;
	}

	/**
	 * Note: The data returned may change in the future. This should be used for debugging purposes, not robust error handling.
	 *
	 * Note: The data returned depends on the encoding. Ascii and RTU encodings are different
	 *
	 * Note: It is possible for this data to be null.
	 * @return The raw data or null. This includes the slave address, the function code, the data, and the checksum
	 */
	public byte[] getRawData() {
		return rawData;
	}
}
