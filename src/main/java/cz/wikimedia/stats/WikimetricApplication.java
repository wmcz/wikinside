package cz.wikimedia.stats;

import cz.wikimedia.stats.dao.ProjectRepository;
import cz.wikimedia.stats.model.Project;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@SpringBootApplication
public class WikimetricApplication {

	private final ProjectRepository projectRepository;

	public WikimetricApplication(ProjectRepository projectRepository) {
		this.projectRepository = projectRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(WikimetricApplication.class, args);
	}

	@Bean
	InitializingBean sendDatabase() {
		return () -> projectRepository
				.findByPath("commons.wikimedia.org")
				.ifPresentOrElse(
						p -> {},
						() -> projectRepository.save(new Project(null, "commonswiki", "commons.wikimedia.org")));
	}

}
