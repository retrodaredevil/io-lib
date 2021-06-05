package me.retrodaredevil.io.modbus;

import static java.util.Objects.requireNonNull;

/**
 * Represents an address and a {@link ModbusMessage}
 */
public final class AddressedModbusMessage {
	private final int address;
	private final ModbusMessage modbusMessage;

	public AddressedModbusMessage(int address, ModbusMessage modbusMessage) {
		this.address = address;
		requireNonNull(this.modbusMessage = modbusMessage, "modbusMessage cannot be null!");
	}

	public int getAddress() {
		return address;
	}

	public ModbusMessage getModbusMessage() {
		return modbusMessage;
	}
}
