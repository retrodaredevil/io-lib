package me.retrodaredevil.io.modbus.handling;

public abstract class BaseStartingDataAddress {
	private final int startingDataAddress;

	protected BaseStartingDataAddress(int startingDataAddress) {
		this.startingDataAddress = startingDataAddress;
	}

	public int getStartingDataAddress() {
		return startingDataAddress;
	}
	protected int getStartingDataAddressHigh() {
		return startingDataAddress >> 8;
	}
	protected int getStartingDataAddressLow() {
		return startingDataAddress & 0xFF;
	}
}
