package graspvis.exception;

public class ViewException extends Exception {
	public static final String ERROR_HEADER = "View Error";
	
	public ViewException() {
		super(ERROR_HEADER);
	}
	
	public ViewException(String message) {
		super(message);
	}
}
