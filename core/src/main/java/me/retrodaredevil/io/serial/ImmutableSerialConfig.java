package me.retrodaredevil.io.serial;

public final class ImmutableSerialConfig implements SerialConfig {

	private final int baudRate;
	private final int dataBits;
	private final StopBits stopBits;
	private final Parity parity;
	private final boolean rts, dtr, rs485;

	public ImmutableSerialConfig(int baudRate, int dataBits, StopBits stopBits, Parity parity, boolean rts, boolean dtr, boolean rs485) {
		this.baudRate = baudRate;
		this.dataBits = dataBits;
		this.stopBits = stopBits;
		this.parity = parity;
		this.rts = rts;
		this.dtr = dtr;
		this.rs485 = rs485;
	}

	@Override
	public int getBaudRateValue() {
		return baudRate;
	}

	@Override
	public Parity getParity() {
		return parity;
	}

	@Override
	public int getDataBitsValue() {
		return dataBits;
	}

	@Override
	public StopBits getStopBits() {
		return stopBits;
	}

	@Override
	public boolean isRTS() {
		return rts;
	}

	@Override
	public boolean isDTR() {
		return dtr;
	}

	@Override
	public boolean isRS485() {
		return rs485;
	}
}
