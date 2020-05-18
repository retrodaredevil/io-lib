package me.retrodaredevil.io.modbus.handling;

import me.retrodaredevil.io.modbus.FunctionCode;
import me.retrodaredevil.io.modbus.ModbusMessage;
import me.retrodaredevil.io.modbus.ModbusMessages;

import static me.retrodaredevil.io.modbus.ModbusMessages.get16BitDataFrom8BitArray;

public class WriteMultipleCoilsHandler implements MessageHandler<Void> {
	private final int dataAddress;
	private final boolean[] coils;

	public WriteMultipleCoilsHandler(int dataAddress, boolean[] coils) {
		this.dataAddress = dataAddress;
		this.coils = coils;
	}

	@Override
	public ModbusMessage createRequest() {
		int numberOfBytes = (coils.length + 7) / 8;
		int[] data = new int[5 + numberOfBytes];
		data[0] = dataAddress >> 8;
		data[1] = dataAddress & 0xFF;

		data[2] = coils.length >> 8;
		data[3] = coils.length & 0xFF;

		data[4] = numberOfBytes;

		for (int i = 0; i < coils.length; i++) {
			boolean value = coils[i];
			if (value) {
				int byteIndex = i / 8;
				int position = i % 8;
				data[5 + byteIndex] |= 1 << position;
			}
		}
		return ModbusMessages.createMessage(FunctionCode.WRITE_MULTIPLE_COILS, data);
	}

	@Override
	public Void handleResponse(ModbusMessage response) {
		if (response.getFunctionCode() != FunctionCode.WRITE_MULTIPLE_COILS) {
			throw new FunctionCodeException(FunctionCode.WRITE_MULTIPLE_COILS, response.getFunctionCode());
		}
		if (response.getData().length != 4) {
			throw new ResponseLengthException(4, response.getData().length);
		}
		int[] data16Bit = get16BitDataFrom8BitArray(response.getData());
		int receivedDataAddress = data16Bit[0];
		int receivedNumberOfCoils = data16Bit[1];

		if (receivedDataAddress != dataAddress) {
			throw new WriteException("receivedDataAddress didn't match dataAddress. receivedDataAddress=" + receivedDataAddress + " dataAddress=" + dataAddress);
		}
		if (receivedNumberOfCoils != coils.length) {
			throw new WriteException("receivedNumberOfCoils didn't match coils.length. receivedNumberOfCoils=" + receivedNumberOfCoils + " coils.length=" + coils.length);
		}
		return null;
	}
}
