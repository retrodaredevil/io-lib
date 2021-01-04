package me.retrodaredevil.io.modbus.handling;

import me.retrodaredevil.io.modbus.ModbusMessage;
import me.retrodaredevil.io.modbus.ModbusMessages;

import java.util.Arrays;

public final class HandleResponseHelper {
	private HandleResponseHelper() { throw new UnsupportedOperationException(); }

	public static void checkResponse(ModbusMessage response, int expectedFunctionCode, int expectedDataLength) {
		int functionCode = response.getFunctionCode();
		if(functionCode != expectedFunctionCode) {
			if (ModbusMessages.isFunctionCodeError(functionCode)) {
				int[] data = response.getData();
				if (data.length == 1) {
					throw new ErrorCodeException(response, expectedFunctionCode, data[0]);
				}
				if (data.length == 0) {
					throw new FunctionCodeException(response, expectedFunctionCode, "Expected to get " + expectedFunctionCode + " as the function code, but actually got " + response.getFunctionCode() + " and got no exception code data!");
				}
				throw new FunctionCodeException(response, expectedFunctionCode, "Expected to get " + expectedFunctionCode + " as the function code, but actually got " + response.getFunctionCode() + ". With exception code data: " + Arrays.toString(data));
			}
			throw new FunctionCodeException(response, expectedFunctionCode);
		}
		int[] data = response.getData();
		if (expectedDataLength != data.length) {
			throw new ParsedResponseDataLengthException(response, expectedDataLength);
		}
	}
}
