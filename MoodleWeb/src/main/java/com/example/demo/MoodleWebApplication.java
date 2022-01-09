package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.example.demo.property.FileStorageProperties;

@SpringBootApplication
@EntityScan("model")
@EnableConfigurationProperties({ FileStorageProperties.class })
public class MoodleWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoodleWebApplication.class, args);
	}

}
