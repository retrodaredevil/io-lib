package me.retrodaredevil.io;

import java.io.InputStream;
import java.io.OutputStream;

import static java.util.Objects.requireNonNull;

/**
 * Represents a {@link InputStream} and an {@link OutputStream}
 * <p>
 * It is expected that throughout the lifetime of this object that calls to both {@link #getInputStream()} and {@link #getOutputStream()}
 * will return the same instance each time it is called.
 */
public interface IOBundle extends AutoCloseable {
	InputStream getInputStream();
	OutputStream getOutputStream();
	
	/**
	 * This should be overridden by subclasses to close the input and output streams
	 * @throws Exception If this cannot be closed
	 */
	@Override
	default void close() throws Exception {
	}
	
	/**
	 * NOTE: The {@link #close()} method will do nothing on the instance returned by this method
	 * @param inputStream The {@link InputStream} to return in {@link #getInputStream()}
	 * @param outputStream The {@link OutputStream} to return in {@link #getOutputStream()}
	 * @return An immutable {@link IOBundle} representing {@code inputStream} and {@code outputStream}
	 */
	static IOBundle of(final InputStream inputStream, final OutputStream outputStream){
		requireNonNull(inputStream);
		requireNonNull(outputStream);
		return new IOBundle() {
			@Override
			public InputStream getInputStream() {
				return inputStream;
			}
			
			@Override
			public OutputStream getOutputStream() {
				return outputStream;
			}
		};
	}
	/**
	 * NOTE: Calling close() will close {@code inputStream} then {@code outputStream}
	 * @param inputStream The {@link InputStream} to return in {@link #getInputStream()}
	 * @param outputStream The {@link OutputStream} to return in {@link #getOutputStream()}
	 * @return An immutable {@link IOBundle} representing {@code inputStream} and {@code outputStream}
	 */
	static IOBundle ofWithClose(final InputStream inputStream, final OutputStream outputStream){
		requireNonNull(inputStream);
		requireNonNull(outputStream);
		return new IOBundle() {
			@Override
			public InputStream getInputStream() {
				return inputStream;
			}

			@Override
			public OutputStream getOutputStream() {
				return outputStream;
			}

			@Override
			public void close() throws Exception {
				inputStream.close();
				outputStream.close();
			}
		};
	}

	final class Defaults {
		private Defaults(){ throw new UnsupportedOperationException(); }
		/**
		 * Represents an {@link IOBundle} that returns {@link System#in} and {@link System#out}
		 * <p>
		 * NOTE: Calling {@link #close()} does nothing.
		 */
		public static IOBundle STANDARD_IN_OUT = new IOBundle() {
			@Override
			public InputStream getInputStream() {
				return System.in;
			}
			
			@Override
			public OutputStream getOutputStream() {
				return System.out;
			}
		};
		/**
		 * Represents an {@link IOBundle} that returns {@link System#in} and {@link System#err}
		 * <p>
		 * NOTE: Calling {@link #close()} does nothing.
		 */
		public static IOBundle STANDARD_IN_ERR = new IOBundle() {
			@Override
			public InputStream getInputStream() {
				return System.in;
			}
			
			@Override
			public OutputStream getOutputStream() {
				return System.err;
			}
		};
	}
}
