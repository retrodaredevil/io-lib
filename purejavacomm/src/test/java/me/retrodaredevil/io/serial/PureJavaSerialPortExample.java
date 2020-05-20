package me.retrodaredevil.io.serial;

public class PureJavaSerialPortExample {
	public static void main(String[] args) throws SerialPortException {
		try (PureJavaCommIOBundle ioBundle = PureJavaCommIOBundle.create("/dev/ttyS10", new SerialConfigBuilder(9600).build())) {

		}
	}
}
