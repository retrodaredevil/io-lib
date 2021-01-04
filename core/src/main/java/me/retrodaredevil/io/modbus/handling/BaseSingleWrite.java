package me.retrodaredevil.io.modbus.handling;

import me.retrodaredevil.io.modbus.ModbusMessage;

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
	 * @throws ParsedResponseException If {@code setDataAddress} is unexpected
	 */
	protected void checkDataAddress(ModbusMessage response, int setDataAddress) {
		if (setDataAddress != dataAddress) {
			throw new WriteException(response, "Expected to write to dataAddress: " + dataAddress + " but actually wrote to " + setDataAddress);
		}
	}
}
