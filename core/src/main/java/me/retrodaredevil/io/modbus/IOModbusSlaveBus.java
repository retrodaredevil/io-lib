package me.retrodaredevil.io.modbus;

import me.retrodaredevil.io.IOBundle;

import java.io.InputStream;
import java.io.OutputStream;

public class IOModbusSlaveBus implements ModbusSlaveBus {
	private final InputStream inputStream;
	private final OutputStream outputStream;

	private final IODataEncoder ioDataEncoder;

	public IOModbusSlaveBus(IOBundle ioBundle, IODataEncoder ioDataEncoder) {
		this(ioBundle.getInputStream(), ioBundle.getOutputStream(), ioDataEncoder);
	}
	public IOModbusSlaveBus(InputStream inputStream, OutputStream outputStream, IODataEncoder ioDataEncoder){
		this.inputStream = inputStream;
		this.outputStream = outputStream;
		this.ioDataEncoder = ioDataEncoder;
	}

	@Override
	public ModbusMessage sendRequestMessage(int address, ModbusMessage message) {
		ioDataEncoder.sendMessage(outputStream, address, message);
		return ioDataEncoder.readMessage(address, inputStream);
	}
}
