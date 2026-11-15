package me.retrodaredevil.io.modbus;

import org.jspecify.annotations.NullMarked;

@NullMarked
public class ImmutableAddressModbusSlave implements ModbusSlave {
	private final int address;
	private final ModbusSlaveBus modbus;
	
	public ImmutableAddressModbusSlave(int address, ModbusSlaveBus modbus) {
		this.address = address;
		this.modbus = modbus;
	}
	
	@Override
	public ModbusMessage sendRequestMessage(ModbusMessage message) {
		return modbus.sendRequestMessage(address, message);
	}
	
}
