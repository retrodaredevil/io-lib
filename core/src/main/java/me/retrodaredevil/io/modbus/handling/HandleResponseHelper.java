package me.retrodaredevil.io.modbus.handling;

import me.retrodaredevil.io.modbus.ModbusMessage;
import me.retrodaredevil.io.modbus.ModbusMessages;

public final class HandleResponseHelper {
	private HandleResponseHelper() { throw new UnsupportedOperationException(); }

	public static void checkResponse(ModbusMessage response, int expectedFunctionCode, int expectedDataLength) {
		int functionCode = response.getFunctionCode();
		if(functionCode != expectedFunctionCode) {
			if (ModbusMessages.isFunctionCodeError(functionCode)) {
				int[] data = response.getData();
				if (data.length == 2) {
					int exceptionCode = (data[1] << 8) | data[0];
					throw new ErrorCodeException(response, expectedFunctionCode, exceptionCode);
				}
			}
			throw new FunctionCodeException(response, expectedFunctionCode);
		}
		int[] data = response.getData();
		if (expectedDataLength != data.length) {
			throw new ParsedResponseDataLengthException(response, expectedDataLength);
		}
	}
}
