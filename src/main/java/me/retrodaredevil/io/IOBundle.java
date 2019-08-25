package me.retrodaredevil.io;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Represents a {@link InputStream} and an {@link OutputStream}
 * <p>
 * It is expected that throughout the lifetime of this object that calls to both {@link #getInputStream()} and {@link #getOutputStream()}
 * will return the same instance each time it is called.
 */
public interface IOBundle {
	InputStream getInputStream();
	OutputStream getOutputStream();
}
