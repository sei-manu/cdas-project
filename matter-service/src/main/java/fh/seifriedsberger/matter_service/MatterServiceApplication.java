package fh.seifriedsberger.matter_service;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class MatterServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MatterServiceApplication.class, args);
	}

	@Bean
	ApplicationRunner applicationRunner(Environment environment) {
		return args -> {
			System.out.println("message from application.properties " + environment.getProperty("spring.datasource.url"));
		};
	}

}
