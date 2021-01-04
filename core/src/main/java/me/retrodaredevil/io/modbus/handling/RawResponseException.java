package me.retrodaredevil.io.modbus.handling;

import me.retrodaredevil.io.modbus.ModbusRuntimeException;

public class RawResponseException extends ModbusRuntimeException {
	private final byte[] rawData;
	public RawResponseException(byte[] rawData, String message) {
		super(message);
		this.rawData = rawData;
	}

	/**
	 * Note: The data returned may change in the future. This should be used for debugging purposes, not robust error handling.
	 * @return The raw data. This includes the slave address, the function code, the data, and the checksum
	 */
	public byte[] getRawData() {
		return rawData;
	}
}
