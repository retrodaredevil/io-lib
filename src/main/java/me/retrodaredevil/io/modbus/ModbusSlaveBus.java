package me.retrodaredevil.io.modbus;

import me.retrodaredevil.io.modbus.handling.MessageHandler;

/**
 * Represents a class to interface with Modbus devices. When using this object, you are the Master and you are communicating
 * with slaves that will respond to messages you send through this interface
 */
public interface ModbusSlaveBus {
	/**
	 * Sends a message to the specified slave
	 * @param address The address of the slave
	 * @param message The message to send to the slave
	 * @return The response from the slave
	 */
	ModbusMessage sendRequestMessage(int address, ModbusMessage message);
	
	default <T> T sendRequestMessage(int address, MessageHandler<T> messageHandler){
		ModbusMessage response = sendRequestMessage(address, messageHandler.createRequest());
		return messageHandler.handleResponse(response);
	}
}
