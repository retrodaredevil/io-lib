package me.retrodaredevil.io.modbus;

import me.retrodaredevil.io.modbus.handling.SingleWriteHandler;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class ModbusTest {
	@Test
	void testLRC(){
		int[] data = new int[] { 17, 3, 0, 107, 0, 3 };
		assertEquals(0x7E, RedundancyUtil.calculateLRC(data));
		
		int sum = 0;
		for(int a : data){
			sum += a;
		}
		sum += 0x7E;
		assertEquals(0, sum & 0xFF);
	}
	@Test
	void testCRC(){
		int[] data = { 0x01, 0x06, 0xE0, 0x1D, 0x00, 0x08};
		
		// In modbus, the low byte is first then the high byte. This is why we have to flip them
		assertEquals(RedundancyUtil.flipCRC(0x2FCA), RedundancyUtil.calculateCRC(data));
	}
	@Test
	void testAsciiEncoding(){
		testDataEncoder(new AsciiDataEncoder());
	}
	@Test
	void testRTUEncoding(){
		RTUDataEncoder encoder = new RTUDataEncoder();
		testDataEncoder(encoder);
		
		ByteArrayInputStream responseStream = new ByteArrayInputStream(new byte[] {
				1, 6, 1, 0, 0, 1, 0x49, (byte) 0xF6
		});
		OutputStream output = new ByteArrayOutputStream();
		ModbusSlave slave = new IOModbusSlave(responseStream, output, encoder);
		
		slave.sendMessage(1, new SingleWriteHandler(0x010A, 1));
	}
	private void testDataEncoder(IODataEncoder encoder){
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ModbusMessage message = ModbusMessages.createMessage((byte) 3, new byte[] {0, 0xA, 0, 1});
		encoder.sendMessage(output, 1, message);
		ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
		ModbusMessage receivedMessage = encoder.readMessage(1, input);
		assertEquals(message, receivedMessage);
	}
	
}
