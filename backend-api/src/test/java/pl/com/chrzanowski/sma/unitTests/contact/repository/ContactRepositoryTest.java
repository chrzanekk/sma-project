package pl.com.chrzanowski.sma.unitTests.contact.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pl.com.chrzanowski.sma.AbstractTestContainers;
import pl.com.chrzanowski.sma.contact.model.Contact;
import pl.com.chrzanowski.sma.contact.repository.ContactRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ContactRepositoryTest extends AbstractTestContainers {

    @Autowired
    private ContactRepository contactRepository;

    @Test
    void deleteById_Positive() {
        // Given
        Contact contact = new Contact();
        contact.setFirstName("Test Contact");
        Contact savedContact = contactRepository.save(contact);

        // When
        contactRepository.deleteById(savedContact.getId());
        Optional<Contact> deletedContact = contactRepository.findById(savedContact.getId());

        // Then
        assertTrue(deletedContact.isEmpty());
    }

    @Test
    void deleteById_Negative() {
        // Given
        Long nonexistentId = 999L;

        // When
        contactRepository.deleteById(nonexistentId);
        Optional<Contact> deletedContact = contactRepository.findById(nonexistentId);

        // Then
        assertTrue(deletedContact.isEmpty());
    }

    @Test
    void findById_Positive() {
        // Given
        Contact contact = new Contact();
        contact.setFirstName("Test Contact");
        Contact savedContact = contactRepository.save(contact);

        // When
        Optional<Contact> foundContact = contactRepository.findById(savedContact.getId());

        // Then
        assertTrue(foundContact.isPresent());
        assertEquals("Test Contact", foundContact.get().getFirstName());
    }

    @Test
    void findById_Negative() {
        // When
        Optional<Contact> foundContact = contactRepository.findById(999L);

        // Then
        assertTrue(foundContact.isEmpty());
    }
}
