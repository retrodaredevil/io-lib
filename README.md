# io-lib
A pure Java Modbus implementation with utilities to simplify serial communication

## Modbus Use
This can be used to send messages to slaves (acting as a master). You can also use this to receive messages
from a master device and send a response back.

This library aims to provide an open source heavily object oriented approach to modbus mappings.

## Features
* Create custom function codes by implementing `MessageHandler`.
* Total freedom to extend any class and override its behavior.
* Supports Ascii and RTU encoding. Also create your own custom encoding by implementing `IODataEncoder`.
* Supports CRC and LRC checksums. Automatically checks CRC while using RTU and LRC while using Ascii.
* Parse ModbusMessages (allows you to easily respond to a master).

## Examples
Building a serial config:
```java
SerialConfig serialConfig = new SerialConfigBuilder(9600) // 9600 baud rate
        .setDataBits(8) // 8 data bits, the default
        .setParity(SerialConfig.Parity.NONE) // no parity, the default
        .setStopBits(SerialConfig.StopBits.ONE) // one stop bit, the default
        .build();
```
Initializing IOBundle with a serial connection
```java
SerialConfig serialConfig = ...;
IOBundle ioBundle = JSerialIOBundle.createPort("/dev/ttyUSB0", serialConfig);
```
Initializing modbus:
```java
IOBundle ioBundle = ...;
ModbusSlaveBus modbus = new IOModbusSlaveBus(ioBundle, new RTUDataEncoder(2000, 20, 4)); // 2 second initial timeout, 20ms timeout for end of message, 4ms sleep
ModbusSlave slave = new ImmutableAddressModbusSlave(options.getModbusAddress(), modbus);
```
Using modbus:
```java
private static final MessageHandler<int[]> BATTERY_VOLTAGE = new ReadRegistersHandler(0x0101, 1);
private final ModbusSlave slave = ...;
public float getBatteryVoltage() {
    return slave.sendRequestMessage(BATTERY_VOLTAGE)[0] / 10.0F;
}
```

## 8 Bit and 16 Bit
Since this project deals with Modbus, there are times when code is dealing with 8 bit data or 16 bit data.
Sometimes it can be difficult to tell which one it is. If we wanted code to be most readable, we would make `byte` represent
8 bit data and `short` represent 16 bit data, right? That would make sense, but it wouldn't be very practical because
we're always dealing with unsigned data, and both of those data types *could* be negative. So, this is why you will
rarely see `short` being used.
* `byte` is always 8 bit data
* `short` is always 16 bit data
* `int` could be either 8 bit or 16 bit

## Projects using this
* [SolarThing](https://github.com/wildmountainfarms/solarthing)

## TODO
* Implement Modbus exception codes and throw Java Exceptions corresponding to them
