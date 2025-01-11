package com.one.literalura;

import com.one.literalura.main.Main;
import com.one.literalura.repository.AuthorRepository;
import com.one.literalura.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

	@Autowired private AuthorRepository autorRepository;
	@Autowired private BookRepository libroRepository;

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		new Main(autorRepository, libroRepository).showMenu();
	}
}