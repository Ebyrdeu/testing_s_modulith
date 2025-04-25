package dev.ebyrdeu.backend.image.internal.repository;

import dev.ebyrdeu.backend.image.internal.model.Image;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends PagingAndSortingRepository<Image, Long> {
}