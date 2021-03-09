package me.retrodaredevil.io.modbus;

import me.retrodaredevil.io.modbus.handling.RawResponseException;

import java.util.Arrays;

public class RedundancyException extends RawResponseException {
	private final int expected;
	private final int actual;
	private RedundancyException(byte[] rawData, String message, int expected, int actual) {
		super(rawData, message);
		this.expected = expected;
		this.actual = actual;
	}

	public static RedundancyException createFrom(byte[] bytes, String redundancyType, int expected, int actual) {
		String extra = bytes == null ? "" : (" bytes: " + Arrays.toString(bytes));
		return new RedundancyException(bytes, "Incorrect " + redundancyType + " checksum. Expected: " + expected + " but got: " + actual + extra, expected, actual);
	}

	/**
	 * @return The expected checksum that was stated in the response
	 */
	public int getExpected() {
		return expected;
	}

	/**
	 * @return The actual checksum that was calculated from the response
	 */
	public int getActual() {
		return actual;
	}
}
