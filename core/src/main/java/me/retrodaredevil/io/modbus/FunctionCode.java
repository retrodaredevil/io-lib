package me.retrodaredevil.io.modbus;

public final class FunctionCode {
	private FunctionCode(){ throw new UnsupportedOperationException(); }

	public static final int READ_COIL = 1;
	public static final int READ_DISCRETE_INPUT = 2;
	public static final int READ_HOLDING_REGISTERS = 3;
	public static final int READ_INPUT_REGISTERS = 4;
	public static final int WRITE_SINGLE_COIL = 5;
	public static final int WRITE_SINGLE_REGISTER = 6;

	public static final int READ_EXCEPTION_STATUS = 7;

	public static final int WRITE_MULTIPLE_COILS = 15;
	public static final int WRITE_MULTIPLE_REGISTERS = 16;


	public static final int PROGRAM_CONTROLLER = 13;
	public static final int POLL_CONTROLLER = 14;
	public static final int REPORT_SLAVE_ID = 17;
}
