package me.retrodaredevil.io.modbus;

import me.retrodaredevil.io.modbus.handling.*;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Random;

public class TcpExample {
	public static void main(String[] args) throws IOException {
		Random random = new Random();
		try (Socket socket = new Socket("localhost", 502)) {
			socket.setSoTimeout(500);
			ModbusSlaveBus modbusSlaveBus = new TcpModbusSlaveBus(socket);

			MessageHandler<int[]> read = new ReadRegistersHandler(100, 1);

			int previous = modbusSlaveBus.sendRequestMessage(1, read)[0];
			System.out.println("Was: " + previous);
			modbusSlaveBus.sendRequestMessage(1, new WriteSingleRegisterHandler(100, random.nextInt(1 << 16)));
			int value = modbusSlaveBus.sendRequestMessage(1, read)[0];
			System.out.println("Now is: " + value);

			modbusSlaveBus.sendRequestMessage(1, new WriteMultipleCoilsHandler(30, new boolean[] { false, true, true }));
			System.out.println("Coil at 30 to 32: " + Arrays.toString(modbusSlaveBus.sendRequestMessage(1, new ReadCoilHandler(30, 3))));
			System.out.println("Discrete input at 30: " + Arrays.toString(modbusSlaveBus.sendRequestMessage(1, new ReadDiscreteInputHandler(30, 1))));
		}
	}
}
