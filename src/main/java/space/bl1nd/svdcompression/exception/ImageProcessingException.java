package space.bl1nd.svdcompression.exception;

/**
 * Failure while reading, transforming, or writing an image.
 */
public class ImageProcessingException extends RuntimeException {
    public ImageProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImageProcessingException(String message) {
        super(message);
    }
}
