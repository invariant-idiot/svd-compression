package space.bl1nd.svdcompression.exception;

/**
 * A concise, user-correctable command or file error.
 */
public class UserInputException extends RuntimeException {
    public UserInputException(String message) {
        super(message);
    }

    public UserInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
