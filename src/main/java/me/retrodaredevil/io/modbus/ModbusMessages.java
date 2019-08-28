package me.retrodaredevil.io.modbus;

import java.util.Arrays;
import java.util.Objects;

public final class ModbusMessages {
	private ModbusMessages(){ throw new UnsupportedOperationException(); }
	
	public static ModbusMessage createMessage(byte functionCode, byte[] byteData){
		int length = byteData.length;
		int[] data = new int[length];
		for(int i = 0; i < length; i++){
			data[i] = byteData[i] & 0xFF;
		}
		return new DefaultModbusMessage(functionCode, data, byteData);
	}
	public static ModbusMessage createMessage(int functionCode, int[] data){
		int length = data.length;
		byte[] byteData = new byte[length];
		for(int i = 0; i < length; i++){
			byteData[i] = (byte) data[i];
		}
		return new DefaultModbusMessage((byte) functionCode, data, byteData);
	}
	
	/**
	 * NOTE: Do not call this for the CRC!
	 * @param data16Bit An array of ints where each element represents a 16 bit number.
	 * @return An array twice the size of {@code data16Bit}
	 */
	public static int[] get8BitDataFrom16BitArray(int... data16Bit){
		int[] r = new int[data16Bit.length * 2];
		for(int i = 0; i < r.length; i++){
			int data = data16Bit[i / 2];
			// NOTE: Except for the CRC, it goes High byte, then Low byte.
			if(i % 2 == 0){ // high byte
				r[i] = (data & 0xFF00) >> 8;
			} else { // low byte
				r[i] = data & 0xFF;
			}
		}
		return r;
	}
	public static int[] get16BitDataFrom8BitArray(int ...data8Bit){
		int originalLength = data8Bit.length;
		if(originalLength % 2 == 1){
			throw new IllegalArgumentException("The length must be a multiple of two! length: " + originalLength);
		}
		int length = originalLength / 2;
		int[] r = new int[length];
		for(int i = 0; i < length; i++){
			int high = data8Bit[i * 2];
			int low = data8Bit[i * 2 + 1];
			if(high > 0xFF || high < 0) throw new IllegalArgumentException("High value at index: " + i + " * 2 + 1 is: " + high);
			if(low > 0xFF || low < 0) throw new IllegalArgumentException("Low value at index: " + i + " * 2 + 1 is: " + low);
			
			r[i] = (high << 8) | low;
		}
		return r;
	}
	
	private static class DefaultModbusMessage implements ModbusMessage {
		
		private final byte functionCode;
		private final int[] data;
		private final byte[] byteData;
		
		private DefaultModbusMessage(byte functionCode, int[] data, byte[] byteData) {
			this.functionCode = functionCode;
			this.data = data;
			this.byteData = byteData;
			
			if(data.length != byteData.length){
				throw new IllegalArgumentException();
			}
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(functionCode, Arrays.hashCode(data));
		}
		
		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof ModbusMessage)) return false;
			ModbusMessage that = (ModbusMessage) o;
			return functionCode == that.getFunctionCode() &&
					Arrays.equals(data, that.getData());
		}
		
		@Override
		public String toString() {
			return "DefaultModbusMessage{" +
					"functionCode=" + getFunctionCode() +
					", data=" + Arrays.toString(data) +
					'}';
		}
		
		@Override
		public int getFunctionCode() {
			return functionCode & 0xFF;
		}
		
		@Override
		public byte getByteFunctionCode() {
			return functionCode;
		}
		
		@Override
		public int[] getData() {
			return data;
		}
		
		@Override
		public byte[] getByteData() {
			return byteData;
		}
	}
}
