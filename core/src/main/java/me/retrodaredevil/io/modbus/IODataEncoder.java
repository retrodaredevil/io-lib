package me.retrodaredevil.io.modbus;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public interface IODataEncoder {
	void sendMessage(OutputStream outputStream, int address, ModbusMessage message);
	default byte[] getOutputOfSendMessage(int address, ModbusMessage message) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		sendMessage(outputStream, address, message);
		return outputStream.toByteArray();
	}

	default ModbusMessage readMessage(int expectedAddress, InputStream inputStream) {
		final byte[] bytes = readBytes(inputStream);
		return parseMessage(expectedAddress, bytes);
	}

	ModbusMessage parseMessage(int expectedAddress, byte[] bytes);
	default AddressedModbusMessage readMessage(InputStream inputStream) {
		final byte[] bytes = readBytes(inputStream);
		return parseMessage(bytes);
	}

	AddressedModbusMessage parseMessage(byte[] bytes);

	/**
	 * Reads the bytes from the given {@link InputStream} usually using timeouts specified upon object creation or reading until an end byte(s) is given.
	 * @param inputStream The input stream
	 * @return
	 */
	byte[] readBytes(InputStream inputStream);
}
