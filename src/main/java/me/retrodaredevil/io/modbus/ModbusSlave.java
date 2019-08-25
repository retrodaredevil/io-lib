package me.retrodaredevil.io.modbus;

public interface ModbusSlave {
	/**
	 * Sends a message to the specified slave
	 * @param address
	 * @param message
	 * @return
	 */
	ModbusMessage sendMessage(int address, ModbusMessage message);
}
