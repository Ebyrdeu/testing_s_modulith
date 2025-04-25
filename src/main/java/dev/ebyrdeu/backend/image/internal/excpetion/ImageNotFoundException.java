package dev.ebyrdeu.backend.image.internal.excpetion;


/**
 * @author Maxim Khnykin
 * @version 1.0
 */
public class ImageNotFoundException extends RuntimeException {
	public ImageNotFoundException(String message) {
		super(message);
	}
}
