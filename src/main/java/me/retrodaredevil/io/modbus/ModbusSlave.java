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
	ModbusMessage sendRequestMessage(ModbusMessage message);
	
	default <T> T sendRequestMessage(MessageHandler<T> messageHandler){
		ModbusMessage response = sendRequestMessage(messageHandler.createRequest());
		return messageHandler.handleResponse(response);
	}
}
