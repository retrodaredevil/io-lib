package me.retrodaredevil.io.serial;

import me.retrodaredevil.io.IOBundle;
import purejavacomm.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PureJavaCommIOBundle implements IOBundle {
	private final SerialPort serialPort;
	private final InputStream inputStream;
	private final OutputStream outputStream;

	public PureJavaCommIOBundle(SerialPort serialPort) throws IOException {
		this.serialPort = serialPort;
		inputStream = serialPort.getInputStream();
		outputStream = serialPort.getOutputStream();
	}
	public static PureJavaCommIOBundle create(String port, SerialConfig serialConfig) throws SerialPortException {
		final CommPortIdentifier portIdentifier;
		try {
			portIdentifier = CommPortIdentifier.getPortIdentifier(port);
		} catch (NoSuchPortException e) {
			throw new SerialPortException(e);
		}
		final CommPort commPort;
		try {
			commPort = portIdentifier.open("io-lib", 0);
		} catch (PortInUseException e) {
			throw new SerialPortException(e);
		}
		final SerialPort serialPort = (SerialPort) commPort; // right now all CommPorts should be SerialPorts (maybe this could be changed in a future version)

		try {
			serialPort.setSerialPortParams(serialConfig.getBaudRateValue(), serialConfig.getDataBitsValue(), convertStopBits(serialConfig.getStopBits()), convertParity(serialConfig.getParity()));
			serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
		} catch (UnsupportedCommOperationException e) {
			throw new SerialPortException(e);
		}
		try {
			if (serialConfig.isRTS()) {
				serialPort.setRTS(true);
			}
			if (serialConfig.isDTR()) {
				serialPort.setDTR(true);
			}
			if (serialConfig.isRS485()) {
				throw new PureJavaIllegalStateException("RS485 not supported");
			}
		} catch (PureJavaIllegalStateException e) {
			throw new SerialPortException("It's likely that RTS or DTR or RS485 is not supported", e);
		}
		try {
			return new PureJavaCommIOBundle(serialPort);
		} catch (IOException e) {
			throw new SerialPortException(e);
		}
	}
	private static int convertStopBits(SerialConfig.StopBits stopBits) {
		switch (stopBits) {
			case ONE: return SerialPort.STOPBITS_1;
			case TWO: return SerialPort.STOPBITS_2;
			case ONE_POINT_FIVE: return SerialPort.STOPBITS_1_5;
		}
		throw new IllegalArgumentException("Unsupported stop bits: " + stopBits);
	}
	private static int convertParity(SerialConfig.Parity parity) {
		switch (parity) {
			case NONE: return SerialPort.PARITY_NONE;
			case ODD: return SerialPort.PARITY_ODD;
			case EVEN: return SerialPort.PARITY_EVEN;
			case MARK: return SerialPort.PARITY_MARK;
			case SPACE: return SerialPort.PARITY_SPACE;
		}
		throw new IllegalArgumentException("Unsupported parity: " + parity);
	}

	@Override
	public InputStream getInputStream() {
		return inputStream;
	}

	@Override
	public OutputStream getOutputStream() {
		return outputStream;
	}

	@Override
	public void close() {
		serialPort.close();
	}
}
