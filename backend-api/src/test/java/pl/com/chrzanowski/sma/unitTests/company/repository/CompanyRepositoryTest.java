package pl.com.chrzanowski.sma.unitTests.company.repository;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pl.com.chrzanowski.sma.AbstractTestContainers;
import pl.com.chrzanowski.sma.company.model.Company;
import pl.com.chrzanowski.sma.company.repository.CompanyRepository;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CompanyRepositoryTest extends AbstractTestContainers {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private Flyway flyway;

    @BeforeEach
    void setUp() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void findByName() {
        //Given
        companyRepository.deleteAll();
        Company company = Company.builder().name("Test").users(Collections.emptySet()).build();
        companyRepository.save(company);

        //When
        var actual = companyRepository.findByName(company.getName()).get();

        //Then
        assertEquals(company, actual);
    }

    @Test
    void findByName_Failure() {
        //Given
        companyRepository.deleteAll();

        //When
        var actual = companyRepository.findByName("Test");

        //Then
        assertEquals(Optional.empty(), actual);
    }
}
