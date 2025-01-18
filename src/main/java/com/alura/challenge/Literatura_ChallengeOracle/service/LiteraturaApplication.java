package com.alura.challenge.Literatura_ChallengeOracle.service;


import com.alura.challenge.Literatura_ChallengeOracle.principal.Principal;
import com.alura.challenge.Literatura_ChallengeOracle.repository.AutorRepository;
import com.alura.challenge.Literatura_ChallengeOracle.repository.LibroRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraturaApplication implements CommandLineRunner {
    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository;

    public static void main(String[] args) {
        SpringApplication.run(LiteraturaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Principal principal = new Principal(libroRepository, autorRepository);
        principal.muestraElMenu();
    }
}
