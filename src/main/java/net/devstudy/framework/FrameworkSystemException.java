package net.devstudy.framework;
/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public class FrameworkSystemException extends RuntimeException {
	private static final long serialVersionUID = -857103561544848290L;

	public FrameworkSystemException(String message) {
		super(message);
	}

	public FrameworkSystemException(Throwable cause) {
		super(cause);
	}

	public FrameworkSystemException(String message, Throwable cause) {
		super(message, cause);
	}

	public FrameworkSystemException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
