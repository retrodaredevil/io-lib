package me.retrodaredevil.io.modbus.handling;

public class RawResponseLengthException extends RawResponseException {
	public RawResponseLengthException(byte[] data, String message){
		super(data, message);
	}
}
