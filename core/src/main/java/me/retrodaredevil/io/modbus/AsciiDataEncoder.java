package me.retrodaredevil.io.modbus;

import me.retrodaredevil.io.modbus.handling.RawResponseLengthException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class AsciiDataEncoder implements IODataEncoder {
	/*
	 * Note that since this is ascii, each byte only ever uses 7 bits, so we (sometimes) don't have to worry about negative numbers here
	 */

	public AsciiDataEncoder() {
	}

	@Override
	public void sendMessage(OutputStream outputStream, int address, ModbusMessage message) {
		char[] chars = toAscii(address, message);
		byte[] bytes = new byte[chars.length];
		for(int i = 0; i < chars.length; i++){
			bytes[i] = (byte) chars[i];
		}
		try {
			outputStream.write(bytes);
			outputStream.flush(); // most serial implementations you don't have to do this, but it's good practice
		} catch (IOException e) {
			throw new ModbusRuntimeException("Got exception while writing", e);
		}
	}
	public static char[] toAscii(int address, ModbusMessage message){
		int functionCode = message.getFunctionCode();
		int[] data = message.getData();
		int lrc = RedundancyUtil.calculateLrc(data);

		int length = 9 + data.length * 2;
		char[] chars = new char[length];
		chars[0] = ':';
		char[] addressAscii = toAscii(address);
		chars[1] = addressAscii[0];
		chars[2] = addressAscii[1];
		char[] functionAscii = toAscii(functionCode);
		chars[3] = functionAscii[0];
		chars[4] = functionAscii[1];

		for(int i = 0; i < data.length; i++){
			char[] ascii = toAscii(data[i]);
			chars[5 + i * 2] = ascii[0];
			chars[6 + i * 2] = ascii[1];
		}
		char[] lrcAscii = toAscii(lrc);
		chars[length - 4] = lrcAscii[0];
		chars[length - 3] = lrcAscii[1];
		chars[length - 2] = '\r';
		chars[length - 1] = '\n';
		return chars;
	}
	private static char[] toAscii(int b){
		int high = (b & 0xF0) >> 4;
		int low = b & 0x0F;
		return new char[] {
				toChar(high),
				toChar(low)
		};
	}
	private static char toChar(int b){
		if(b < 10){
			return (char) (b + 0x30);
		}
//		return (char) (b + 65 - 10);
		return (char) (b + 55);
	}

	/**
	 *
	 * @param expectedAddress The expected address in the data
	 * @param bytes The ascii data between the ':' and '\r' Not including ':', '\r', or '\n'
	 * @return The parsed modbus message
	 */
	public static ModbusMessage fromAscii(int expectedAddress, byte[] bytes){
		if (bytes.length < 6) {
			throw new RawResponseLengthException(bytes, "Unexpected length: " + bytes.length + ". bytes: " + Arrays.toString(bytes) + ". We expected address: " + expectedAddress);
		}
		int address = fromAscii(bytes[0], bytes[1]);
		int functionCode = fromAscii(bytes[2], bytes[3]);
		int length = (bytes.length - 6) / 2;
		int[] data = new int[length];
		for(int i = 0; i < length; i++){
			byte high = bytes[i * 2 + 4];
			byte low = bytes[i * 2 + 5];
			data[i] = fromAscii(high, low);
		}
		int expectedLrc = fromAscii(bytes[bytes.length - 2], bytes[bytes.length - 1]);
		if(address != expectedAddress){
			throw new UnexpectedSlaveResponseException(expectedAddress, address);
		}
		int actualLrc = RedundancyUtil.calculateLrc(data);
		if(expectedLrc != actualLrc){
			throw new RedundancyException("LRC", expectedLrc, actualLrc, "bytes: " + Arrays.toString(bytes));
		}
		return ModbusMessages.createMessage(functionCode, data);
	}
	private static int parseDigit(byte asciiValue) {
		if (asciiValue > 'F') {
			throw new ModbusRuntimeException("Ascii value: " + asciiValue + " is not valid!");
		}
		if (asciiValue >= 'A') {
			return asciiValue - 55;
		}
		if (asciiValue < 0x30 || asciiValue > 0x39) {
			throw new ModbusRuntimeException("Ascii value: " + asciiValue + " is not valid!");
		}
		return asciiValue - 0x30;
	}
	private static int fromAscii(byte high, byte low){
		return (parseDigit(high) << 4) + parseDigit(low);
	}

	@Override
	public ModbusMessage readMessage(int expectedAddress, InputStream inputStream) {
		final byte[] bytes;
		try {
			bytes = readLine(inputStream);
		} catch (IOException e) {
			throw new ModbusRuntimeException("Got exception while reading", e);
		}
		return fromAscii(expectedAddress, bytes);
	}
	private byte[] readLine(InputStream inputStream) throws IOException{
		// TODO add optional timeout parameters to class
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		boolean started = false;
		boolean cr = false;
		while(true){
			int next = inputStream.read();
			if(next == -1){
				throw new ModbusRuntimeException("End of stream!");
			}
			if(!started){
				started = next == ':';
			} else {
				if(cr){
					if(next != '\n'){
						throw new ModbusRuntimeException("Next character should be a new line!");
					}
					break;
				} else if(next == '\r'){
					cr = true;
				} else {
					bytes.write((byte) next);
				}
			}
		}
		return bytes.toByteArray();
	}
}
