package me.retrodaredevil.io.modbus;

public final class ExceptionCode {
	private ExceptionCode() { throw new UnsupportedOperationException(); }
	// http://www.simplymodbus.ca/exceptions.htm

	public static int ILLEGAL_FUNCTION = 1;
	public static int ILLEGAL_DATA_ACCESS = 2;
	public static int ILLEGAL_DATA_VALUE = 3;
	public static int SLAVE_DEVICE_FAILURE = 4;
	public static int ACKNOWLEDGE = 5;
	public static int SLAVE_DEVICE_BUSY = 6;
	public static int NEGATIVE_ACKNOWLEDGE = 7;
	public static int MEMORY_PARITY_ERROR = 8;
	// skip 9 for some reason
	public static int GATEWAY_PATH_UNAVAILABLE = 10;
	public static int GATEWAY_TARGET_DEVICE_FAILED_TO_RESPOND = 11;
}
