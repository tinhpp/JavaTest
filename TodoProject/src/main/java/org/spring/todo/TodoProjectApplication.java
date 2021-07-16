package org.spring.todo;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TodoProjectApplication {

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
        .setMatchingStrategy(MatchingStrategies.STRICT);
		return modelMapper;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(TodoProjectApplication.class, args);
	}

}
