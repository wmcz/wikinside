package cz.wikimedia.stats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class WikimetricApplication {

	public static void main(String[] args) {
		SpringApplication.run(WikimetricApplication.class, args);
	}

}
