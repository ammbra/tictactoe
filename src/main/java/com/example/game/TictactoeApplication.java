package com.example.game;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.reflect.Field;
import java.security.Security;
import java.util.Properties;

@SpringBootApplication
public class TictactoeApplication {

	public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
		SpringApplication.run(TictactoeApplication.class, args);
	}

}
