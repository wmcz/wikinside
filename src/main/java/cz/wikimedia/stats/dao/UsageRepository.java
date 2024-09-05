package cz.wikimedia.stats.dao;

import cz.wikimedia.stats.model.GlobalPage;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsageRepository extends CrudRepository<GlobalPage, Long> {
    Optional<GlobalPage> findByWikiUrlAndTitle(String wikiUrl, String title);
}
