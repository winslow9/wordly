package customExceptions.GameProcessExceptions;

public class WrongLengthException extends Exception {
    public WrongLengthException(String message) {
        super(message);
    }
}
