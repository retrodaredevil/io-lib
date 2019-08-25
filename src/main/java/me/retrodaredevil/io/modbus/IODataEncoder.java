package me.retrodaredevil.io.modbus;

import java.io.InputStream;
import java.io.OutputStream;

public interface IODataEncoder {
	void sendMessage(OutputStream outputStream, int address, ModbusMessage message);
	ModbusMessage readMessage(int expectedAddress, InputStream inputStream);
}
