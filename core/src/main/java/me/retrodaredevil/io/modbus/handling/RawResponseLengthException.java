package me.retrodaredevil.io.modbus.handling;

import org.jspecify.annotations.NullMarked;

@NullMarked
public class RawResponseLengthException extends RawResponseException {
	public RawResponseLengthException(byte[] data, String message){
		super(data, message);
	}
}
