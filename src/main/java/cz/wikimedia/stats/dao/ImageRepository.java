package cz.wikimedia.stats.dao;

import cz.wikimedia.stats.model.Image;
import org.springframework.data.repository.CrudRepository;

public interface ImageRepository extends CrudRepository<Image, Long> {}
