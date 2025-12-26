package spring.load;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "spring.load")
public class LoadTestMqApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoadTestMqApplication.class, args);
	}

}

