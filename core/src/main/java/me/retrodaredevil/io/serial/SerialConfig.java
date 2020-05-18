package me.retrodaredevil.io.serial;

public interface SerialConfig {
	int getBaudRateValue();
	Parity getParity();
	int getDataBitsValue();
	StopBits getStopBits();
	boolean isRTS();
	boolean isDTR();
	
	// TODO implement flow control
	
	enum Baud {
		B1200(1200), B2400(2400), B4800(4800), B9600(9600), B19200(19200),
		B38400(38400), B56000(56000), B115200(115200), B128000(128000), B256000(256000)
		;
		private final int rate;
		Baud(int rate) {
			this.rate = rate;
		}
		public int getRate(){ return rate; }
		public static Baud getBaud(int rate){
			for(Baud b : values()){
				if(b.rate == rate){
					return b;
				}
			}
			return null;
		}
	}
	enum Parity {
		NONE,
		ODD,
		EVEN,
		MARK,
		SPACE
	}
	enum StopBits {
		ONE,
		ONE_POINT_FIVE,
		TWO
	}
}
