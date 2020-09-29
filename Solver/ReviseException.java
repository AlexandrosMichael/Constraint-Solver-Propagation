/**
 * Custom exception. Thrown when an arc revision causes a variable to have an empty domain value set.
 */
public class ReviseException extends Exception {

    public ReviseException(String message) {
        super(message);
    }
}
