package me.retrodaredevil.io.modbus;

public class UnexpectedTransactionIdException extends InvalidResponseException {
	public UnexpectedTransactionIdException(int expectedTransactionId, int receivedTransactionId) {
		super("Unexpected transaction id! expected: " + expectedTransactionId + " got: " + receivedTransactionId);
	}
}
