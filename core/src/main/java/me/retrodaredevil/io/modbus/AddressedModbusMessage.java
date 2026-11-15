package me.retrodaredevil.io.modbus;

import org.jspecify.annotations.NullMarked;

import static java.util.Objects.requireNonNull;

/**
 * Represents an address and a {@link ModbusMessage}
 */
@NullMarked
public final class AddressedModbusMessage {
	private final int address;
	private final ModbusMessage modbusMessage;

	public AddressedModbusMessage(int address, ModbusMessage modbusMessage) {
		this.address = address;
		this.modbusMessage = requireNonNull(modbusMessage, "modbusMessage cannot be null!");
	}

	public int getAddress() {
		return address;
	}

	public ModbusMessage getModbusMessage() {
		return modbusMessage;
	}
}
