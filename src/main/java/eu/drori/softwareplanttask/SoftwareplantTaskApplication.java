package eu.drori.softwareplanttask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SoftwareplantTaskApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(SoftwareplantTaskApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SoftwareplantTaskApplication.class);
	}

}