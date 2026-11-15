package me.retrodaredevil.io.modbus;

import me.retrodaredevil.io.modbus.handling.MessageHandler;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Represents communication to a single ModbusSlave. The implementation can decide how to send and respond to the message
 */
@NullMarked
public interface ModbusSlave {
	/**
	 * Sends a message to the specified slave
	 * @param message The request message
	 * @return The response
	 * @throws ModbusRuntimeException if something went wrong while making the request
	 */
	ModbusMessage sendRequestMessage(ModbusMessage message);

	/**
	 * Sends a message to the specified slave and handles the response
	 * @param messageHandler The {@link MessageHandler}.
	 * @return The handled response
	 * @throws ModbusRuntimeException if something went wrong while making the request or while handling the request
	 */
	default <T extends @Nullable Object> T sendRequestMessage(MessageHandler<T> messageHandler){
		ModbusMessage response = sendRequestMessage(messageHandler.createRequest());
		return messageHandler.handleResponse(response);
	}
}
