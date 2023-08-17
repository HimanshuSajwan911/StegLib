/**
 * Author: Himanshu Sajwan
 * GitHub: https://github.com/HimanshuSajwan911
 */

package io.github.himanshusajwan911.util.exceptions;

/**
 * Exception thrown to indicate that there are insufficient bytes available for a specific operation.
 */
public class InsufficientBytesException extends RuntimeException{

    /**
     * Constructs an {@code InsufficientBytesException} with no detail message.
     */
    public InsufficientBytesException() {
        super();
    }

    /**
     * Constructs an {@code InsufficientBytesException} with the specified detail message.
     *
     * @param message the detail message.
     */
    public InsufficientBytesException(String message) {
        super(message);
    }

    /**
     * Constructs an {@code InsufficientBytesException} with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause the cause (a {@code Throwable} that is the underlying cause of this exception).
     */
    public InsufficientBytesException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
