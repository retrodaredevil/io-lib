# io-lib
A pure Java Modbus implementation with utilities to simplify serial communication

## Modbus Use
Currently, the Modbus part of the library can only act as a master that communications with slaves. In the future,
this might be able to act as a slave

Be aware that not all the functions of this library have been tested and I am not responsible for any damage done
from bugs in this library. If you need to use this for an industrial application, you should go with a library
that has been tested. This library aims to provide an open source heavily object oriented approach to modbus mappings.

## Advantages of this library
* Create custom function codes by implementing `MessageHandler`
* Total freedom to extend any class and override its behavior
* Supports Ascii and RTU encoding. Also create your own custom encoding by implementing `IODataEncoder`
* Supports CRC and LRC checksums. Automatically checks one or the other based on encoding type (Ascii or RTU)

## TODO
* Implement Modbus exception codes and throw Java Exceptions corresponding to them
