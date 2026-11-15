package me.retrodaredevil.io.modbus;

import me.retrodaredevil.io.modbus.handling.RawResponseException;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Should be thrown when a different slave responds than expected
 */
@NullMarked
public class UnexpectedSlaveResponseException extends RawResponseException {
	private UnexpectedSlaveResponseException(byte @Nullable [] rawData, String message) {
		super(rawData, message);
	}

	public static UnexpectedSlaveResponseException fromAddresses(byte @Nullable [] data, int expectedAddress, int slaveResponseAddress) {
		return new UnexpectedSlaveResponseException(data, "Address: " + (expectedAddress & 0xFF) + " was expected but slave with address: " + (slaveResponseAddress & 0xFF) + " responded!");
	}
	public static UnexpectedSlaveResponseException fromAddressesNoData(int expectedAddress, int slaveResponseAddress) {
		return fromAddresses(null, expectedAddress, slaveResponseAddress);
	}
}
