package pl.com.chrzanowski.sma.unitTests.role.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import pl.com.chrzanowski.sma.AbstractTestContainers;
import pl.com.chrzanowski.sma.common.enumeration.ERole;
import pl.com.chrzanowski.sma.role.model.Role;
import pl.com.chrzanowski.sma.role.repository.RoleRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class RoleRepositoryTest extends AbstractTestContainers {

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        roleRepository.deleteAll();
    }

    @Test
    void findByName() {
        //Given
        Role role = Role.builder().name(ERole.ROLE_USER).build();
        roleRepository.save(role);


        //When
        var actual = roleRepository.findByName(ERole.ROLE_USER).get();


        //Then
        assertEquals(role, actual);
    }

    @Test
    void findByNameWithFail() {
        //Given

        //When
        var actual = roleRepository.findByName(ERole.ROLE_USER);


        //Then
        assertEquals(Optional.empty(), actual);
    }
}