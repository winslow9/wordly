package custom.exceptions.game.process.exceptions;

public class WrongLengthException extends RuntimeException {
    public WrongLengthException(String message) {
        super(message);
    }
}
