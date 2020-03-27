package me.retrodaredevil.io.serial;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortInvalidPortException;
import me.retrodaredevil.io.IOBundle;

import java.io.InputStream;
import java.io.OutputStream;

import static java.util.Objects.requireNonNull;

public class JSerialIOBundle implements IOBundle {
	private final InputStream inputStream;
	private final OutputStream outputStream;
	private final SerialPort serialPort;
	
	public JSerialIOBundle(SerialPort serialPort, SerialConfig serialConfig) throws SerialPortException {
		this.serialPort = serialPort;
		if(!serialPort.openPort(0)){
			throw new SerialPortException("Was unsuccessful while trying to open port: " + serialPort.getSystemPortName() + " descriptive name: " + serialPort.getDescriptivePortName() + " description: " + serialPort.getPortDescription());
		}
		final int stopBits;
		switch(serialConfig.getStopBits()){
			case ONE: stopBits = SerialPort.ONE_STOP_BIT; break;
			case ONE_POINT_FIVE: stopBits = SerialPort.ONE_POINT_FIVE_STOP_BITS; break;
			case TWO: stopBits = SerialPort.TWO_STOP_BITS; break;
			default: throw new UnsupportedOperationException("Unknown stop bits: " + serialConfig.getStopBits());
		}
		final int parity;
		switch(serialConfig.getParity()){
			case NONE: parity = SerialPort.NO_PARITY; break;
			case ODD: parity = SerialPort.ODD_PARITY; break;
			case EVEN: parity = SerialPort.EVEN_PARITY; break;
			case MARK: parity = SerialPort.MARK_PARITY; break;
			case SPACE: parity = SerialPort.SPACE_PARITY; break;
			default: throw new UnsupportedOperationException("Unknown parity: " + serialConfig.getParity());
		}
		serialPort.setComPortParameters(serialConfig.getBaudRateValue(), serialConfig.getDataBitsValue(), stopBits, parity);
		if(serialConfig.isRTS()){
			serialPort.setRTS();
		} else {
			serialPort.clearRTS();
		}
		if(serialConfig.isDTR()){
			serialPort.setDTR();
		} else {
			serialPort.clearDTR();
		}
		inputStream = requireNonNull(serialPort.getInputStream());
		outputStream = requireNonNull(serialPort.getOutputStream());
	}
	
	public static JSerialIOBundle createFromPortIndex(int index, SerialConfig serialConfig) throws SerialPortException {
		final SerialPort[] ports;
		try {
			ports = SerialPort.getCommPorts();
		} catch(SerialPortInvalidPortException e){
			throw new SerialPortException(e);
		}
		if(index >= ports.length){
			throw new SerialPortException("There are only " + ports.length + " serial ports! index=" + index);
		}
		SerialPort port = ports[index];
		return new JSerialIOBundle(port, serialConfig);
	}
	public static JSerialIOBundle createPort(String port, SerialConfig serialConfig) throws SerialPortException {
		final SerialPort serialPort;
		try {
			serialPort = SerialPort.getCommPort(port);
		} catch(SerialPortInvalidPortException e){
			throw new SerialPortException("invalid port! port: " + port, e);
		}
		return new JSerialIOBundle(serialPort, serialConfig);
	}
	
	@Override
	public void close() {
		serialPort.closePort();
	}
	
	@Override
	public InputStream getInputStream() {
		return inputStream;
	}
	
	@Override
	public OutputStream getOutputStream() {
		return outputStream;
	}
}
