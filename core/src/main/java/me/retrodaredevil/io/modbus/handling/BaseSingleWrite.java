package me.retrodaredevil.io.modbus.handling;

public abstract class BaseSingleWrite {
	private final int dataAddress;

	protected BaseSingleWrite(int dataAddress) {
		this.dataAddress = dataAddress;
	}

	public int getDataAddress() {
		return dataAddress;
	}
	protected int getDataAddressHigh() {
		return dataAddress >> 8;
	}
	protected int getDataAddressLow() {
		return dataAddress & 0xFF;
	}

	/**
	 * @param setDataAddress The data address in the response
	 * @throws ResponseException If {@code setDataAddress} is unexpected
	 */
	protected void checkDataAddress(int setDataAddress) {
		if (setDataAddress != dataAddress) {
			throw new WriteException("Expected to write to dataAddress: " + dataAddress + " but actually wrote to " + setDataAddress);
		}
	}
}
