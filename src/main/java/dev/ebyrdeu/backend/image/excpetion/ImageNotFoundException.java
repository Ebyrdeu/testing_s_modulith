package dev.ebyrdeu.backend.image.excpetion;


/**
 * @author Maxim Khnykin
 * @version 1.0
 */
public class ImageNotFoundException extends RuntimeException {
	public ImageNotFoundException(String message) {
		super(message);
	}
}
