# io-lib
A pure Java Modbus implementation with utilities to simplify serial communication

## Modbus Use
This can be used to send messages to slaves (acting as a master). You can also use this to receive messages
from a master device and send a response back.

This library aims to provide an open source heavily object oriented approach to modbus mappings.

## Features
* Create custom function codes by implementing `MessageHandler`.
* Total freedom to extend any class and override its behavior.
* Supports Ascii, RTU, and TCP encoding. Also create your own custom encoding by implementing `IODataEncoder`.
* Supports CRC and LRC checksums. Automatically checks CRC while using RTU and LRC while using Ascii.
* Parse ModbusMessages (allows you to easily respond to a master).
* Modbus logic is not coupled to serial logic.
* Uses common interfaces. This makes it easy to swap out implementations. Decide to switch from Ascii encoding to using
TCP? No problem.

## Drawbacks
* Not set up for asynchronous requests
* Not set up for multiple requests at once for TCP (you must request and wait for response)

## Importing
[![](https://jitpack.io/v/retrodaredevil/io-lib.svg)](https://jitpack.io/#retrodaredevil/io-lib)
```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.retrodaredevil.io-lib:core:<LATEST RELEASE>'
    implementation 'com.github.retrodaredevil.io-lib:jSerialComm:<LATEST RELEASE>'
}
```


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
ModbusSlaveBus modbus = new IOModbusSlaveBus(ioBundle, new RtuDataEncoder(2000, 20, 4)); // 2 second initial timeout, 20ms timeout for end of message, 4ms sleep
ModbusSlave slave = new ImmutableAddressModbusSlave(options.getModbusAddress(), modbus);
```
Using modbus:
```java
private static final MessageHandler<int[]> BATTERY_VOLTAGE = new ReadRegisters(0x0101, 1);
private final ModbusSlave slave = ...;
public float getBatteryVoltage() {
    return slave.sendRequestMessage(BATTERY_VOLTAGE)[0] / 10.0F;
}
```

## Exceptions
There are many places in this library where checked exceptions are thrown. Such as `MessageParseException`s, `SerialPortException`s.
However, you should also be aware of `ModbusRuntimeException`s. These can pop up in just about any place that deals with Modbus.
These are runtime exceptions for convenience. You likely aren't able to deal with them when they first
pop up, so you usually handle them later up the call stack.

## Using Asynchronously
This library doesn't deal with threads at all. Everything is set up to be synchronous. However, if you want to use this
asynchronously, you can set up your own way of executing a request asynchronously. This library won't fight you.
Since almost everything in this library is immutable you usually don't have to worry about putting locks on
objects because they don't have mutable state. 

If you do use this asynchronously, remember that you cannot make two requests to two different devices because they
may come back out of order, which the library does not support.

## 8 Bit and 16 Bit
Since this project deals with Modbus, there are times when code is dealing with 8 bit data or 16 bit data.
Sometimes it can be difficult to tell which one it is. If we wanted code to be most readable, we would make `byte` represent
8 bit data and `short` represent 16 bit data, right? That would make sense, but it wouldn't be very practical because
we're always dealing with unsigned data, and both of those data types *could* be negative. So, this is why you will
rarely see `short` being used.
* `byte` is always 8 bit data
* `short` is always 16 bit data
* `int` could be either 8 bit or 16 bit

## Testing this library
If you want to test this library, you can use https://www.modbusdriver.com/diagslave.html

* Testing TCP: `sudo ./diagslave -m tcp -a 0 -p 502` (address of 0 is valid for TCP)

## Projects using this
* [SolarThing](https://github.com/wildmountainfarms/solarthing)

## TODO
* Implement Modbus exception codes and throw Java Exceptions corresponding to them
* Support two byte slave addressing
