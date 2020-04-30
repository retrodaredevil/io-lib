package me.retrodaredevil.io.modbus.handling;

import me.retrodaredevil.io.modbus.ModbusMessage;

public interface MessageResponseCreator<T> extends MessageHandler<T> {
    /**
     * Creates a response message with the specified data
     * @param data The data to put in the returned response message
     * @return A response {@link ModbusMessage} with the specified data
     */
    ModbusMessage createResponse(T data);
}
