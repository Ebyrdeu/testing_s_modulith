package dev.ebyrdeu.backend.image.internal.model;

import dev.ebyrdeu.backend.common.entity.DefaultEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Entity
@Table(name = "images")
public class Image extends DefaultEntity {

	@Size(max = 50)
	@NotNull
	@Column(name = "title", nullable = false, length = 50)
	private String title;

	@Column(name = "description", length = Integer.MAX_VALUE)
	private String description;

	@Column(name = "price", precision = 10, scale = 2)
	private BigDecimal price;

	@Size(max = 255)
	@Column(name = "image_url")
	private String imageUrl;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

}