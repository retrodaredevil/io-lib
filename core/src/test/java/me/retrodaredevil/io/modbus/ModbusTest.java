package me.retrodaredevil.io.modbus;

import me.retrodaredevil.io.modbus.handling.*;
import me.retrodaredevil.io.modbus.parsing.DefaultMessageParser;
import me.retrodaredevil.io.modbus.parsing.MessageParseException;
import me.retrodaredevil.io.modbus.parsing.MessageParser;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import static me.retrodaredevil.io.modbus.ModbusMessages.get16BitDataFrom8BitArray;
import static org.junit.jupiter.api.Assertions.*;

final class ModbusTest {
	@Test
	void testModbusMessages(){
		assertEquals(133, ModbusMessages.createMessage(133, new int[]{}).getFunctionCode());
	}
	@Test
	void testLrc(){
		int[] data = new int[] { 17, 3, 0, 107, 0, 3 };
		assertEquals(0x7E, RedundancyUtil.calculateLrc(data));

		int sum = 0;
		for(int a : data){
			sum += a;
		}
		sum += 0x7E;
		assertEquals(0, sum & 0xFF);
	}
	@Test
	void testCrc(){
		int[] data = { 0x01, 0x06, 0xE0, 0x1D, 0x00, 0x08};

		// In modbus, the low byte is first then the high byte. This is why we have to flip them
//		assertEquals(RedundancyUtil.flipCrc(0x2FCA), RedundancyUtil.calculateCRC(data));
		assertEquals(0xCA2F, RedundancyUtil.calculateCrc(data));
	}
	@Test
	void testAsciiEncoding(){
		testDataEncoder(new AsciiDataEncoder());
	}
	@Test
	void testRtuEncoding(){
		RtuDataEncoder encoder = new RtuDataEncoder();
		testDataEncoder(encoder);
		{ // test writing values
			ByteArrayInputStream responseStream = new ByteArrayInputStream(new byte[]{
					1,
					6,
					1, 10,
					0, 1,
					0x69, (byte) 0xF4
			});
			OutputStream output = new ByteArrayOutputStream();
			ModbusSlaveBus slave = new IOModbusSlaveBus(responseStream, output, encoder);

			slave.sendRequestMessage(1, new WriteSingleRegister(0x010A, 1));
		}
		{ // test writing multiple values
			int crc = RedundancyUtil.calculateCrc(1, 16, 0x01, 0x0A, 0, 2);
			System.out.println(crc);
			ByteArrayInputStream responseStream = new ByteArrayInputStream(new byte[]{
					1,
					16, // function code
					0x01, 0x0A, // starting address
					0, 2, // 2 registers
					(byte) (crc & 0xFF), (byte) ((crc & 0xFF00) >> 8)
			});
			OutputStream output = new ByteArrayOutputStream();
			ModbusSlaveBus slave = new IOModbusSlaveBus(responseStream, output, encoder);

			slave.sendRequestMessage(1, new WriteMultipleRegisters(0x010A, new int[] {
					31, 71, 98, 43
			}));
		}
		{ // test reading values
			int crc = RedundancyUtil.calculateCrc(1, 3, 3 * 2, 99, 67, 85, 45, 92, 91);
			System.out.println(crc);
			ByteArrayInputStream responseStream = new ByteArrayInputStream(new byte[]{
					1,
					3, // function code
					3 * 2, // 3 registers * 2 bytes each = 6 bytes
					99, 67, 85, 45, 92, 91,
					(byte) (crc & 0xFF), (byte) ((crc & 0xFF00) >> 8)
			});
			OutputStream output = new ByteArrayOutputStream();
			ModbusSlaveBus slave = new IOModbusSlaveBus(responseStream, output, encoder);

			ReadHoldingRegisters readHoldingRegisters = new ReadHoldingRegisters(0x010A, 3);
			ModbusMessage message = readHoldingRegisters.createRequest();
			ModbusMessage response = slave.sendRequestMessage(1, message); // this will have a length of 3
			int[] registers = readHoldingRegisters.handleResponse(response);
			assertArrayEquals(get16BitDataFrom8BitArray(99, 67, 85, 45, 92, 91), registers);

			ModbusMessage expectedResponse = readHoldingRegisters.createResponse(registers);
			assertEquals(expectedResponse.getFunctionCode(), response.getFunctionCode());
			assertArrayEquals(expectedResponse.getData(), response.getData());
		}
	}
	/** A method to test encoding and decoding messages */
	private void testDataEncoder(IODataEncoder encoder){
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ModbusMessage message = ModbusMessages.createMessage((byte) 3, new byte[] {0, 0xA, 0, 1});
		encoder.sendMessage(output, 1, message);
		ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
		ModbusMessage receivedMessage = encoder.readMessage(1, input);
		assertEquals(message, receivedMessage);
	}
	@Test
	void testResponse() {
		{
			int[] exampleData = new int[] { 0x20F, 43, 0xFFF, 0x1FA0};
			assertArrayEquals(exampleData, getResponseData(new ReadHoldingRegisters(0xEA0, 4), exampleData));
		}
		assertNull(getResponseData(new WriteSingleRegister(0xEA0, 0xFFFF), null));
		assertNull(getResponseData(new WriteMultipleRegisters(0xEA0, new int[] { 127, 127, 0, 43}), null));
	}
	private <T> T getResponseData(MessageResponseCreator<T> messageResponseCreator, T data) {
		ModbusMessage requestMessage = messageResponseCreator.createRequest();
		ModbusMessage exampleResponse = messageResponseCreator.createResponse(data);
		assertEquals(requestMessage.getFunctionCode(), exampleResponse.getFunctionCode());
		return messageResponseCreator.handleResponse(exampleResponse);
	}

	@Test
	void testParse() throws MessageParseException {
		MessageParser parser = new DefaultMessageParser();
		testParser(parser, new ReadCoils(5, 5));
		testParser(parser, new ReadDiscreteInputs(5, 5));
		testParser(parser, new ReadHoldingRegisters(5, 1));
		testParser(parser, new WriteSingleCoil(5, true));
		testParser(parser, new WriteSingleRegister(5, 2));
		testParser(parser, new WriteMultipleCoils(5, new boolean[] { true, true, false }));
		testParser(parser, new WriteMultipleRegisters(5, new int[] { 127, 127, 0, 43}));
	}
	private void testParser(MessageParser parser, MessageHandler<?> messageHandler) throws MessageParseException {
		MessageHandler<?> result = parser.parseRequestMessage(messageHandler.createRequest());
		assertEquals(messageHandler, result);
	}

}
