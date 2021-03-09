package me.retrodaredevil.io.modbus;

import me.retrodaredevil.io.modbus.handling.RawResponseLengthException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class RtuDataEncoder implements IODataEncoder {
	private final long initialTimeout;
	private final long endMillis;
	private final long sleepTime;
	public RtuDataEncoder(long initialTimeout, long endMillis, long sleepTime){
		this.initialTimeout = initialTimeout;
		this.endMillis = endMillis;
		this.sleepTime = sleepTime;
		if(sleepTime < 0){
			throw new IllegalArgumentException("sleepTime cannot be less than 0! sleepTime=" + sleepTime);
		}
	}
	public RtuDataEncoder(long initialTimeout, long endMillis){
		this(initialTimeout, endMillis, 3);
	}
	public RtuDataEncoder(){
		this(1000, 10);
	}
	@Override
	public void sendMessage(OutputStream outputStream, int address, ModbusMessage message) {
		byte[] bytes = toBytes(address, message);
		try {
			outputStream.write(bytes);
			outputStream.flush(); // most serial implementations you don't have to do this, but it's good practice
		} catch (IOException e) {
			throw new ModbusIORuntimeException("Got exception while writing", e);
		}
	}
	public static byte[] toBytes(int address, ModbusMessage message){
		byte code = message.getByteFunctionCode();
		byte[] data = message.getByteData();
		int crc = RedundancyUtil.calculateCrc(getCrcBytes((byte) address, code, data));
		byte highCrc = (byte) ((crc & 0xFF00) >> 8);
		byte lowCrc = (byte) (crc & 0xFF);
		byte[] bytes = new byte[4 + data.length];
		bytes[0] = (byte) address;
		bytes[1] = code;

		System.arraycopy(data, 0, bytes, 2, data.length);
		bytes[data.length + 2] = lowCrc;
		bytes[data.length + 3] = highCrc;
		return bytes;
	}

	@Override
	public ModbusMessage readMessage(int expectedAddress, InputStream inputStream) {
		final byte[] bytes;
		try {
			bytes = readBytes(inputStream);
		} catch(IOException e){
			throw new ModbusIORuntimeException("Got exception while reading", e);
		}
		return fromBytes(expectedAddress, bytes);
	}
	public static ModbusMessage fromBytes(int expectedAddress, byte[] bytes){
		int length = bytes.length;
		if (length < 4) {
			throw new RawResponseLengthException(bytes, "Unexpected length: " + length + ". bytes: " + Arrays.toString(bytes) + ". We expected address: " + expectedAddress);
		}
		byte address = bytes[0];
		// the & 0xFF probably doesn't matter, since if it's an exception, address is negative, making the result of (address != expectedAddress)
		//   is the same when expected address is in range [0, 127], but hey, let's do it anyway
		if((address & 0xFF) != expectedAddress){
			throw UnexpectedSlaveResponseException.fromAddresses(bytes, expectedAddress, address & 0xFF);
		}
		byte code = bytes[1];
		byte[] data = new byte[length - 4];
		System.arraycopy(bytes, 2, data, 0, length - 4);
		int lowCrc = bytes[length - 2] & 0xFF;
		int highCrc = bytes[length - 1] & 0xFF;
		int expectedCrc = 0xFFFF & ((highCrc << 8) | lowCrc);
		int actualCrc = RedundancyUtil.calculateCrc(getCrcBytes(address, code, data));
		if(expectedCrc != actualCrc){
			throw RedundancyException.createFrom(bytes, "CRC", expectedCrc, actualCrc);
		}

		return ModbusMessages.createMessage(code, data);
	}
	private static byte[] getCrcBytes(byte address, byte functionCode, byte[] data){
		byte[] crcBytes = new byte[data.length + 2];
		crcBytes[0] = address;
		crcBytes[1] = functionCode;
		System.arraycopy(data, 0, crcBytes, 2, data.length);
		return crcBytes;
	}
	private byte[] readBytes(InputStream inputStream) throws IOException {
		byte[] buffer = new byte[1024];
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		long startTime = System.currentTimeMillis();
		Long lastData = null;
		while(true){
			if(inputStream.available() != 0){
				int len = inputStream.read(buffer);
				if(len <= 0){
					throw new AssertionError("We check InputStream#available()! len should not be <= 0! It's: " + len);
				}
				lastData = System.currentTimeMillis();
				bytes.write(buffer, 0, len);
			} else {
				long currentTime = System.currentTimeMillis();
				if(lastData == null){ // not started
					if(startTime + initialTimeout < currentTime){
						throw new ModbusTimeoutException("Timed out! startTime=" + startTime + " currentTime=" + currentTime + " initialTimeout=" + initialTimeout);
					}
				} else {
					if(lastData + endMillis < currentTime){
						return bytes.toByteArray();
					}
				}
			}
			if(sleepTime != 0) {
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException ex) {
					throw new RuntimeException(ex);
				}
			}
		}
	}
}
