package graspvis.exception;

public class SystemException extends Exception {
	public static final String ERROR_HEADER = "System Error";

	public SystemException() {
		super(ERROR_HEADER);
	}

	public SystemException(String message) {
		super(message);
	}
}
