package dev.ebyrdeu.backend.image.internal.management;

import dev.ebyrdeu.backend.image.ImageExternalApi;
import dev.ebyrdeu.backend.image.internal.repository.ImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
class ImageManagement implements ImageExternalApi {

	private static final Logger log = LoggerFactory.getLogger(ImageManagement.class);
	private final ImageRepository imageRepository;

	public ImageManagement(ImageRepository imageRepository) {
		this.imageRepository = imageRepository;
	}

}
