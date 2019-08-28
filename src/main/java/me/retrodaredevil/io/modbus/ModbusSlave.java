package me.retrodaredevil.io.modbus;

import me.retrodaredevil.io.modbus.handling.MessageHandler;

/**
 * Represents communication to a single ModbusSlave. The implementation can decide how to send and respond to the message
 */
public interface ModbusSlave {
	/**
	 * Sends a message to the specified slave
	 * @param message
	 * @return
	 */
	ModbusMessage sendMessage(ModbusMessage message);
	
	default <T> T sendMessage(MessageHandler<T> messageHandler){
		ModbusMessage response = sendMessage(messageHandler.createMessage());
		return messageHandler.handleResponse(response);
	}
}
