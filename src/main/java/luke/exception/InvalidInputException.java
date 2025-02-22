package luke.exception;

/**
 * This exception is thrown to indicate that an invalid input
 * has been provided to a method or operation.
 * <p>
 * Use this exception to handle cases where input validation fails.
 * For example, when a required parameter is null or empty.
 * </p>
 */
public class InvalidInputException extends Exception {
    public InvalidInputException(String message) {
        super(message);
    }

    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidInputException(Throwable cause) {
        super(cause);
    }
}
