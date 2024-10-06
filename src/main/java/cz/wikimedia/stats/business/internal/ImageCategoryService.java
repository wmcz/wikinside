package cz.wikimedia.stats.business.internal;

import cz.wikimedia.stats.dao.ImageCategoryRepository;
import cz.wikimedia.stats.model.ImageCategory;
import org.springframework.stereotype.Service;

@Service
public class ImageCategoryService extends InternalService<ImageCategory, String> {
    public ImageCategoryService(ImageCategoryRepository repository) {
        super(repository);
    }

    public ImageCategory processCategory(String name) {
        return repository.save(new ImageCategory(name));
    }
}
