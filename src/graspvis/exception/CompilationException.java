package graspvis.exception;

public class CompilationException extends Exception {
	public CompilationException() {
		super("Compilation error");
	}
	
	public CompilationException(String exception) {
		super(exception);
	}
}
