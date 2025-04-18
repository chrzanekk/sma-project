package pl.com.chrzanowski.sma.unitTests.contact.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Path;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.com.chrzanowski.sma.contact.dao.ContactJPADaoImpl;
import pl.com.chrzanowski.sma.contact.model.Contact;
import pl.com.chrzanowski.sma.contact.model.QContact;
import pl.com.chrzanowski.sma.contact.repository.ContactRepository;
import pl.com.chrzanowski.sma.contact.service.filter.ContactQuerySpec;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContactJPADaoImplTest {

    @InjectMocks
    private ContactJPADaoImpl contactJPADaoImpl;

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private ContactQuerySpec contactQuerySpec;

    @Mock
    private EntityManager em;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void save_Positive() {
        Contact contact = new Contact();
        when(contactRepository.save(contact)).thenReturn(contact);

        Contact savedContact = contactJPADaoImpl.save(contact);

        assertEquals(contact, savedContact);
        verify(contactRepository, times(1)).save(contact);
    }

    @Test
    void save_Negative() {
        Contact contact = new Contact();
        when(contactRepository.save(contact)).thenThrow(new RuntimeException("Save failed"));

        assertThrows(RuntimeException.class, () -> contactJPADaoImpl.save(contact));
        verify(contactRepository, times(1)).save(contact);
    }

    @Test
    void findById_Positive() {
        Long id = 1L;
        Contact contact = new Contact();
        when(contactRepository.findById(id)).thenReturn(Optional.of(contact));

        Optional<Contact> foundContact = contactJPADaoImpl.findById(id);

        assertTrue(foundContact.isPresent());
        assertEquals(contact, foundContact.get());
        verify(contactRepository, times(1)).findById(id);
    }

    @Test
    void findById_Negative() {
        Long id = 999L;
        when(contactRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Contact> foundContact = contactJPADaoImpl.findById(id);

        assertTrue(foundContact.isEmpty());
        verify(contactRepository, times(1)).findById(id);
    }

    @Test
    void findAll_Positive() {
        Contact contact = new Contact();
        when(contactRepository.findAll()).thenReturn(List.of(contact));

        List<Contact> contacts = contactJPADaoImpl.findAll();

        assertEquals(1, contacts.size());
        assertEquals(contact, contacts.getFirst());
        verify(contactRepository, times(1)).findAll();
    }

    @Test
    void findAll_Negative() {
        when(contactRepository.findAll()).thenReturn(Collections.emptyList());

        List<Contact> contacts = contactJPADaoImpl.findAll();

        assertTrue(contacts.isEmpty());
        verify(contactRepository, times(1)).findAll();
    }

    @Test
    void findAllWithSpecificationAndPageable_Positive() {
        BooleanBuilder specification = new BooleanBuilder();
        Pageable pageable = PageRequest.of(0, 10);
        Contact contact = new Contact();

        @SuppressWarnings("unchecked")
        JPAQuery<Contact> mockQuery = mock(JPAQuery.class);
        when(mockQuery.leftJoin(eq(QContact.contact.contractor), any(Path.class))).thenReturn(mockQuery);
        when(mockQuery.leftJoin(any(EntityPath.class))).thenReturn(mockQuery);
        when(mockQuery.fetchJoin()).thenReturn(mockQuery);


        when(contactQuerySpec.buildQuery(specification, pageable)).thenReturn(mockQuery);
        when(mockQuery.fetchCount()).thenReturn(1L);
        when(mockQuery.offset(pageable.getOffset())).thenReturn(mockQuery);
        when(mockQuery.limit(pageable.getPageSize())).thenReturn(mockQuery);

        when(mockQuery.fetch()).thenReturn(List.of(contact));

        Page<Contact> result = contactJPADaoImpl.findAll(specification, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(contact, result.getContent().getFirst());
        verify(contactQuerySpec, times(1)).buildQuery(specification, pageable);
    }

    @Test
    void findAllWithSpecificationAndPageable_Negative() {
        BooleanBuilder specification = new BooleanBuilder();
        Pageable pageable = PageRequest.of(0, 10);

        @SuppressWarnings("unchecked")
        JPAQuery<Contact> mockQuery = mock(JPAQuery.class);
        when(mockQuery.leftJoin(eq(QContact.contact.contractor), any(Path.class))).thenReturn(mockQuery);
        when(mockQuery.leftJoin(any(EntityPath.class))).thenReturn(mockQuery);
        when(mockQuery.fetchJoin()).thenReturn(mockQuery);

        when(contactQuerySpec.buildQuery(specification, pageable)).thenReturn(mockQuery);
        when(mockQuery.fetchCount()).thenReturn(0L);
        when(mockQuery.offset(pageable.getOffset())).thenReturn(mockQuery);
        when(mockQuery.limit(pageable.getPageSize())).thenReturn(mockQuery);
        when(mockQuery.fetch()).thenReturn(Collections.emptyList());

        Page<Contact> result = contactJPADaoImpl.findAll(specification, pageable);

        assertTrue(result.isEmpty());
        verify(contactQuerySpec, times(1)).buildQuery(specification, pageable);
    }

    @Test
    void deleteById_Positive() {
        Long id = 1L;

        contactJPADaoImpl.deleteById(id);

        verify(contactRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteById_Negative() {
        Long id = 999L;
        doThrow(new RuntimeException("Delete failed")).when(contactRepository).deleteById(id);

        assertThrows(RuntimeException.class, () -> contactJPADaoImpl.deleteById(id));
        verify(contactRepository, times(1)).deleteById(id);
    }
}
