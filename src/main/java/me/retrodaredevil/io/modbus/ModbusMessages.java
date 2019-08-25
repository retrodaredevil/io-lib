package me.retrodaredevil.io.modbus;

import java.util.Arrays;
import java.util.Objects;

public final class ModbusMessages {
	private ModbusMessages(){ throw new UnsupportedOperationException(); }
	
	public static ModbusMessage createMessage(byte functionCode, byte[] byteData){
		int length = byteData.length;
		int[] data = new int[length];
		for(int i = 0; i < length; i++){
			data[i] = byteData[i];
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
			if(data.length % 2 != 0){
				throw new IllegalArgumentException("length of the data must be a multiple of 2!");
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
					"functionCode=" + functionCode +
					", data=" + Arrays.toString(data) +
					'}';
		}
		
		@Override
		public int getFunctionCode() {
			return functionCode;
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
