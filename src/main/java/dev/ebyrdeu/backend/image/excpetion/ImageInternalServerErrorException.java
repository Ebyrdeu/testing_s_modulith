package dev.ebyrdeu.backend.image.excpetion;


/**
 * @author Maxim Khnykin
 * @version 1.0
 */
public class ImageInternalServerErrorException extends RuntimeException {
	public ImageInternalServerErrorException(String message) {
		super(message);
	}
}
