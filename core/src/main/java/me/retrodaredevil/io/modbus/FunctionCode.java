package me.retrodaredevil.io.modbus;

public final class FunctionCode {
	private FunctionCode(){ throw new UnsupportedOperationException(); }
	
	public static final int READ_COIL = 1;
	public static final int READ_DISCRETE_INPUT = 2;
	public static final int READ_REGISTERS = 3;
	public static final int WRITE_SINGLE_COIL = 5;
	public static final int WRITE_SINGLE_REGISTER = 6;
	public static final int WRITE_MULTIPLE_COILS = 15;
	public static final int WRITE_MULTIPLE_REGISTERS = 16;
}
