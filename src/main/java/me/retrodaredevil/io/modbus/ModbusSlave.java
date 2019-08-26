package me.retrodaredevil.io.modbus;

import me.retrodaredevil.io.modbus.handling.MessageHandler;

public interface ModbusSlave {
	/**
	 * Sends a message to the specified slave
	 * @param address
	 * @param message
	 * @return
	 */
	ModbusMessage sendMessage(int address, ModbusMessage message);
	
	default <T> T sendMessage(int address, MessageHandler<T> messageHandler){
		ModbusMessage response = sendMessage(address, messageHandler.createMessage());
		return messageHandler.handleResponse(response);
	}
}
