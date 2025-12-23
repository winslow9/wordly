package custom.exceptions.game.process.exceptions;

public class WordNotFoundInDictionary extends RuntimeException {
    public WordNotFoundInDictionary(String message) {
        super(message);
    }
}
