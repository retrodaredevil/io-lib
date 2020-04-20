package me.retrodaredevil.io.modbus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RTUDataEncoder implements IODataEncoder {
	private final long initialTimeout;
	private final long endMillis;
	private final long sleepTime;
	public RTUDataEncoder(long initialTimeout, long endMillis, long sleepTime){
		this.initialTimeout = initialTimeout;
		this.endMillis = endMillis;
		this.sleepTime = sleepTime;
		if(sleepTime < 0){
			throw new IllegalArgumentException("sleepTime cannot be less than 0! sleepTime=" + sleepTime);
		}
	}
	public RTUDataEncoder(long initialTimeout, long endMillis){
		this(initialTimeout, endMillis, 3);
	}
	public RTUDataEncoder(){
		this(1000, 10);
	}
	@Override
	public void sendMessage(OutputStream outputStream, int address, ModbusMessage message) {
		byte[] bytes = toBytes(address, message);
		try {
			outputStream.write(bytes);
		} catch (IOException e) {
			throw new ModbusRuntimeException(e);
		}
	}
	public static byte[] toBytes(int address, ModbusMessage message){
		byte code = message.getByteFunctionCode();
		byte[] data = message.getByteData();
		int crc = RedundancyUtil.calculateCRC(getCrcBytes((byte) address, code, data));
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
			throw new RuntimeException(e);
		}
		return fromBytes(expectedAddress, bytes);
	}
	public static ModbusMessage fromBytes(int expectedAddress, byte[] bytes){
		int length = bytes.length;
		byte address = bytes[0];
		byte code = bytes[1];
		byte[] data = new byte[length - 4];
		System.arraycopy(bytes, 2, data, 0, length - 4);
		int lowCrc = bytes[length - 2] & 0xFF;
		int highCrc = bytes[length - 1] & 0xFF;
		if(address != expectedAddress){
			throw new UnexpectedSlaveResponseException(expectedAddress, address);
		}
		int expectedCrc = 0xFFFF & ((highCrc << 8) | lowCrc);
		int actualCrc = RedundancyUtil.calculateCRC(getCrcBytes(address, code, data));
		if(expectedCrc != actualCrc){
			throw new RedundancyException("CRC", expectedCrc, actualCrc, "bytes: " + Arrays.toString(bytes));
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
		List<Byte> bytes = new ArrayList<>();
		long startTime = System.currentTimeMillis();
		Long lastData = null;
		while(true){
			if(inputStream.available() != 0){
				int len = inputStream.read(buffer);
				if(len <= 0){
					throw new AssertionError("We check InputStream#available()! len should not be <= 0! It's: " + len);
				}
				lastData = System.currentTimeMillis();
				for(int i = 0; i < len; i++){
					bytes.add(buffer[i]);
				}
			} else {
				long currentTime = System.currentTimeMillis();
				if(lastData == null){ // not started
					if(startTime + initialTimeout < currentTime){
						throw new ModbusTimeoutException("Timed out! startTime=" + startTime + " currentTime=" + currentTime + " initialTimeout=" + initialTimeout);
					}
				} else {
					if(lastData + endMillis < currentTime){
						int size = bytes.size();
						byte[] r = new byte[size];
						for(int i = 0; i < size; i++){
							r[i] = bytes.get(i);
						}
						return r;
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
