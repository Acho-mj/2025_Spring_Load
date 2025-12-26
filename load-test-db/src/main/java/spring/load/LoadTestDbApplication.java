package spring.load;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "spring.load")
@EnableJpaRepositories(basePackages = "spring.load.domain")
public class LoadTestDbApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoadTestDbApplication.class, args);
	}

}

