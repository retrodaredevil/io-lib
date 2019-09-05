package me.retrodaredevil.io;

import java.io.InputStream;
import java.io.OutputStream;

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
	 *
	 * @throws Exception If this cannot be closed
	 */
	@Override
	default void close() throws Exception {
		getInputStream().close();
		getOutputStream().close();
	}
}
