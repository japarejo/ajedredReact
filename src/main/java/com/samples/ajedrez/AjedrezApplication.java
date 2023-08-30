package com.samples.ajedrez;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//You must scan for Spring annotations when linking with external jar
// Removed these two string and will crash when running app

@SpringBootApplication(scanBasePackages = { "com.samples.ajedrez", "io.github.isagroup" })
public class AjedrezApplication {

	public static void main(String[] args) {
		SpringApplication.run(AjedrezApplication.class, args);
	}

}
