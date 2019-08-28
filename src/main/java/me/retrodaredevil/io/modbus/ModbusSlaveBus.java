package me.retrodaredevil.io.modbus;

import me.retrodaredevil.io.modbus.handling.MessageHandler;

/**
 * Represents a class to interface with Modbus devices. When using this object, you are the Master and you are communicating
 * with slaves that will respond to messages you send through this interface
 */
public interface ModbusSlaveBus {
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
