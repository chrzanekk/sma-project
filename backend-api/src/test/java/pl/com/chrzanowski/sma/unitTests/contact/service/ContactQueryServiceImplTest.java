package pl.com.chrzanowski.sma.unitTests.contact.service;

import com.querydsl.core.BooleanBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.com.chrzanowski.sma.contact.dao.ContactDao;
import pl.com.chrzanowski.sma.contact.dto.ContactAuditableDTO;
import pl.com.chrzanowski.sma.contact.dto.ContactDTO;
import pl.com.chrzanowski.sma.contact.mapper.ContactAuditMapper;
import pl.com.chrzanowski.sma.contact.model.Contact;
import pl.com.chrzanowski.sma.contact.service.ContactQueryServiceImpl;
import pl.com.chrzanowski.sma.contact.service.filter.ContactFilter;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ContactQueryServiceImplTest {

    @Mock
    private ContactDao contactDao;

    @Mock
    private ContactAuditMapper contactAuditMapper;

    @InjectMocks
    private ContactQueryServiceImpl contactQueryService;

    private ContactDTO contactDTO;
    private ContactAuditableDTO contactAuditableDTO;
    private Contact contact;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        contactDTO = ContactDTO.builder()
                .id(1L)
                .firstName("John Doe")
                .build();

        contactAuditableDTO = ContactAuditableDTO.builder()
                .base(contactDTO).build();

        contact = new Contact();
        contact.setId(1L);
        contact.setFirstName("John Doe");
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testFindByFilterSuccess() {
        ContactFilter filter = new ContactFilter();

        when(contactDao.findAll(any(BooleanBuilder.class))).thenReturn(Collections.singletonList(contact));
        when(contactAuditMapper.toDtoList(anyList())).thenReturn(Collections.singletonList(contactAuditableDTO));

        List<ContactAuditableDTO> result = contactQueryService.findByFilter(filter);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(contactDao, times(1)).findAll(any(BooleanBuilder.class));
        verify(contactAuditMapper, times(1)).toDtoList(anyList());
    }

    @Test
    void testFindByFilterEmpty() {
        ContactFilter filter = new ContactFilter();

        when(contactDao.findAll(any(BooleanBuilder.class))).thenReturn(Collections.emptyList());
        when(contactAuditMapper.toDtoList(anyList())).thenReturn(Collections.emptyList());

        List<ContactAuditableDTO> result = contactQueryService.findByFilter(filter);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(contactDao, times(1)).findAll(any(BooleanBuilder.class));
    }

    @Test
    void testFindByFilterAndPageSuccess() {
        ContactFilter filter = new ContactFilter();
        Pageable pageable = PageRequest.of(0, 10);

        Page<Contact> contactPage = new PageImpl<>(Collections.singletonList(contact));
        when(contactDao.findAll(any(BooleanBuilder.class), any(Pageable.class))).thenReturn(contactPage);
        when(contactAuditMapper.toDto(any(Contact.class))).thenReturn(contactAuditableDTO);

        Page<ContactAuditableDTO> result = contactQueryService.findByFilter(filter, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        verify(contactDao, times(1)).findAll(any(BooleanBuilder.class), any(Pageable.class));
        verify(contactAuditMapper, times(1)).toDto(any(Contact.class));
    }

    @Test
    void testFindByFilterAndPageEmpty() {
        ContactFilter filter = new ContactFilter();
        Pageable pageable = PageRequest.of(0, 10);

        Page<Contact> contactPage = Page.empty();
        when(contactDao.findAll(any(BooleanBuilder.class), any(Pageable.class))).thenReturn(contactPage);

        Page<ContactAuditableDTO> result = contactQueryService.findByFilter(filter, pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(contactDao, times(1)).findAll(any(BooleanBuilder.class), any(Pageable.class));
    }
}
