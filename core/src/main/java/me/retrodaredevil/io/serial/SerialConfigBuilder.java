package me.retrodaredevil.io.serial;

import static java.util.Objects.requireNonNull;

public class SerialConfigBuilder implements SerialConfig {

	private int baudRate;
	private int dataBits = 8;
	private StopBits stopBits = StopBits.ONE;
	private Parity parity = Parity.NONE;
	private boolean rts = false, dtr = false, rs485 = false;

	public SerialConfigBuilder(SerialConfig serialConfig){
		baudRate = serialConfig.getBaudRateValue();
		dataBits = serialConfig.getDataBitsValue();
		stopBits = serialConfig.getStopBits();
		parity = serialConfig.getParity();
		rts = serialConfig.isRTS();
		dtr = serialConfig.isDTR();
	}
	public SerialConfigBuilder(int baudRate){
		this.baudRate = baudRate;
	}

	public SerialConfig build(){
		return new ImmutableSerialConfig(baudRate, dataBits, stopBits, parity, rts, dtr, rs485);
	}
	@Override
	public int getBaudRateValue() {
		return baudRate;
	}
	public SerialConfigBuilder setBaudRate(int baudRate){
		this.baudRate = baudRate;
		return this;
	}
	public SerialConfigBuilder setBaudRate(Baud baudRate){
		this.baudRate = baudRate.getRate();
		return this;
	}
	@Override
	public Parity getParity() {
		return parity;
	}
	public SerialConfigBuilder setParity(Parity parity){
		this.parity = requireNonNull(parity);
		return this;
	}
	@Override
	public int getDataBitsValue() {
		return dataBits;
	}
	public SerialConfigBuilder setDataBits(int dataBits){
		this.dataBits = dataBits;
		return this;
	}
	@Override
	public StopBits getStopBits() {
		return stopBits;
	}
	public SerialConfigBuilder setStopBits(StopBits stopBits){
		this.stopBits = stopBits;
		return this;
	}
	@Override
	public boolean isRTS() {
		return rts;
	}
	public SerialConfigBuilder setRTS(boolean b){
		this.rts = b;
		return this;
	}

	@Override
	public boolean isDTR() {
		return dtr;
	}
	public SerialConfigBuilder setDTR(boolean b){
		this.dtr = b;
		return this;
	}

	@Override
	public boolean isRS485() {
		return rs485;
	}
	public SerialConfigBuilder setRS485(boolean isRS485) {
		this.rs485 = isRS485;
		return this;
	}
}
