package me.retrodaredevil.io.modbus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

import static me.retrodaredevil.io.modbus.ModbusMessages.get16BitDataFrom8BitArray;

public class TcpModbusSlaveBus implements ModbusSlaveBus {
	private final AtomicInteger transactionIdCount = new AtomicInteger(0);
	private final Socket socket;

	/**
	 * NOTE: You should have a timeout in place on the socket
	 * @param socket The socket
	 */
	public TcpModbusSlaveBus(Socket socket) {
		this.socket = socket;
	}

	private void writeData(int transactionId, int address, ModbusMessage message) throws IOException {
		OutputStream outputStream = socket.getOutputStream();
		byte[] writeData = new byte[8 + message.getByteData().length];
		writeData[0] = (byte) (transactionId >> 8);
		writeData[1] = (byte) (transactionId & 0xFF);
		writeData[2] = 0;
		writeData[3] = 0;
		int length = 2 + message.getByteData().length;
		writeData[4] = (byte) (length >> 8);
		writeData[5] = (byte) (length & 0xFF);
		writeData[6] = (byte) address;
		writeData[7] = message.getByteFunctionCode();
		byte[] data = message.getByteData();
		System.arraycopy(data, 0, writeData, 8, data.length);
		outputStream.write(writeData);
		outputStream.flush();
	}
	private ModbusMessage readData(int expectedTransactionId, int address) throws IOException {
		InputStream inputStream = socket.getInputStream();
		int receivedTransactionId = get16BitDataFrom8BitArray(checkValue(inputStream.read()), checkValue(inputStream.read()))[0];
		if (receivedTransactionId != expectedTransactionId) {
			throw new UnexpectedTransactionIdException(expectedTransactionId, receivedTransactionId);
		}
		checkValue(inputStream.read());
		checkValue(inputStream.read());
		int length = get16BitDataFrom8BitArray(checkValue(inputStream.read()), checkValue(inputStream.read()))[0];
		if (length <= 3) {
			throw new ModbusRuntimeException("length <= 3! We expected to at least get a function code! length=" + length + ". Transaction ID: " + receivedTransactionId);
		}
		int unitId = checkValue(inputStream.read());
		if (unitId != address) {
			throw UnexpectedSlaveResponseException.fromAddressesNoData(address, unitId);
		}
		int functionCode = checkValue(inputStream.read());
		byte[] data = new byte[length - 2];
		for (int i = 0; i < data.length; i++) {
			data[i] = (byte) checkValue(inputStream.read());
		}
		return ModbusMessages.createMessage((byte) functionCode, data);
	}
	private int checkValue(int value) {
		if (value < 0) {
			throw new ModbusIORuntimeException("Got a value of " + value + "! The stream must have closed!");
		}
		return value;
	}

	@Override
	public ModbusMessage sendRequestMessage(int address, ModbusMessage message) {
		int transactionId = transactionIdCount.getAndIncrement();
		try {
			writeData(transactionId, address, message);
		} catch (IOException e) {
			throw new ModbusIORuntimeException("Got exception while writing", e);
		}
		try {
			return readData(transactionId, address);
		} catch (IOException e) {
			throw new ModbusTimeoutException("Got exception while reading", e);
		}
	}
}
