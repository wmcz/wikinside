package cz.wikimedia.stats.dao;

import cz.wikimedia.stats.model.UserTag;
import org.springframework.data.repository.CrudRepository;

public interface UserTagRepository extends CrudRepository<UserTag, Long> {}
