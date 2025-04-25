package dev.ebyrdeu.backend.image.internal.web;

import dev.ebyrdeu.backend.image.ImageExternalApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/images")
class ImageController {

	private final ImageExternalApi imageExternalApi;

	public ImageController(ImageExternalApi imageExternalApi) {
		this.imageExternalApi = imageExternalApi;
	}
}
