package cz.wikimedia.stats.business.internal;

import cz.wikimedia.stats.dao.UsageRepository;
import cz.wikimedia.stats.model.GlobalPage;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsageService extends InternalService<GlobalPage, Long> {

    private final UsageRepository usageRepository;

    public UsageService(UsageRepository repository) {
        super(repository);
        this.usageRepository = repository;
    }

    public Optional<GlobalPage> findByWikiUrlAndTitle(String wikiUrl, String title) {
        return usageRepository.findByWikiUrlAndTitle(wikiUrl, title);
    }

    public GlobalPage processUsage(String wikiUrl, String title) {
        return usageRepository
                .findByWikiUrlAndTitle(wikiUrl, title)
                .orElseGet(() -> create(new GlobalPage(wikiUrl, title)).get());
    }
}
