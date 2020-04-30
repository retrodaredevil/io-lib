package me.retrodaredevil.io.modbus;

import me.retrodaredevil.io.IOBundle;

import java.io.InputStream;
import java.io.OutputStream;

public class IOModbusSlaveBus implements ModbusSlaveBus {
	private final InputStream inputStream;
	private final OutputStream outputStream;
	
	private final IODataEncoder formatter;
	
	public IOModbusSlaveBus(IOBundle ioBundle, IODataEncoder formatter) {
		this(ioBundle.getInputStream(), ioBundle.getOutputStream(), formatter);
	}
	public IOModbusSlaveBus(InputStream inputStream, OutputStream outputStream, IODataEncoder formatter){
		this.inputStream = inputStream;
		this.outputStream = outputStream;
		this.formatter = formatter;
	}
	
	@Override
	public ModbusMessage sendRequestMessage(int address, ModbusMessage message) {
		formatter.sendMessage(outputStream, address, message);
		return formatter.readMessage(address, inputStream);
	}
}
