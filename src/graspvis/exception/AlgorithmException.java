package graspvis.exception;

public class AlgorithmException extends Exception {
	public static final String ERROR_HEADER = "Algorithm Error";

	public AlgorithmException() {
		super(ERROR_HEADER);
	}

	public AlgorithmException(String message) {
		super(message);
	}
}
