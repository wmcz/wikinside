package cz.wikimedia.stats.dao;

import cz.wikimedia.stats.model.ImageCategory;
import org.springframework.data.repository.CrudRepository;

public interface ImageCategoryRepository extends CrudRepository<ImageCategory, String> {}
