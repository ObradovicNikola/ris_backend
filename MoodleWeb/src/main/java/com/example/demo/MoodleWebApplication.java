package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

import com.example.demo.property.FileStorageProperties;

@SpringBootApplication
@EnableAsync
@EntityScan("model")
@EnableConfigurationProperties({ FileStorageProperties.class })
public class MoodleWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoodleWebApplication.class, args);
	}

}
