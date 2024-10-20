package pl.com.chrzanowski.scma;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SmaApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        SpringApplication.run(SmaApplication.class, args);
    }
}
