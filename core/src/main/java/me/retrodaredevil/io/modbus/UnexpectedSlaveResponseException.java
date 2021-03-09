package me.retrodaredevil.io.modbus;

import me.retrodaredevil.io.modbus.handling.RawResponseException;

/**
 * Should be thrown when a different slave responds than expected
 */
public class UnexpectedSlaveResponseException extends RawResponseException {
	private UnexpectedSlaveResponseException(byte[] rawData, String message) {
		super(rawData, message);
	}

	public static UnexpectedSlaveResponseException fromAddresses(byte[] data, int expectedAddress, int slaveResponseAddress) {
		return new UnexpectedSlaveResponseException(data, "Address: " + (expectedAddress & 0xFF) + " was expected but slave with address: " + (slaveResponseAddress & 0xFF) + " responded!");
	}
	public static UnexpectedSlaveResponseException fromAddressesNoData(int expectedAddress, int slaveResponseAddress) {
		return fromAddresses(null, expectedAddress, slaveResponseAddress);
	}
}
