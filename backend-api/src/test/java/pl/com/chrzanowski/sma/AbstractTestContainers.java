package pl.com.chrzanowski.sma;

import org.flywaydb.core.Flyway;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

public abstract class AbstractTestContainers {

    @Container
    protected static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("postgres-test")
            .withUsername("test")
            .withPassword("test");

    static {
        postgreSQLContainer.start();
        runFlywayMigrations();
    }

    @DynamicPropertySource
    protected static void setProperties(DynamicPropertyRegistry registry) {
        // Konfiguracja bazy danych
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");

        // Konfiguracja Flyway
        registry.add("spring.flyway.enabled", () -> "true");
        registry.add("spring.flyway.locations", () -> "classpath:db/migration");
        registry.add("spring.flyway.clean-disabled", () -> "false");

        // Konfiguracja maila
        registry.add("spring.mail.host", () -> "localhost");
        registry.add("spring.mail.port", () -> "1025");
        registry.add("spring.mail.username", () -> "test@test.pl");
        registry.add("spring.mail.password", () -> "test");
        registry.add("spring.mail.protocol", () -> "smtp");

        // Konfiguracja JWT
        registry.add("jwt.jwtSecret", () -> "verylongsecretJWTsecretkeywithmanydigitsandletterstocorrectsizecheckofsecretkey");
        registry.add("jwt.jwtExpirationMs", () -> "86400000");
        registry.add("jwt.tokenValidityTimeInMinutes", () -> "15");
    }

    private static void runFlywayMigrations() {
        Flyway flyway = Flyway.configure()
                .dataSource(postgreSQLContainer.getJdbcUrl(),
                        postgreSQLContainer.getUsername(),
                        postgreSQLContainer.getPassword())
                .locations("classpath:db/migration")
                .cleanDisabled(false)
                .load();

        // Wyczyść schemat i wykonaj migracje
        flyway.clean();
        flyway.migrate();
    }
}
