package pl.com.chrzanowski.sma.contact.dao;

import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.com.chrzanowski.sma.contact.model.Contact;

import java.util.List;
import java.util.Optional;

public interface ContactDao {

    Contact save(Contact contact);

    List<Contact> saveAll(List<Contact> contacts);

    Optional<Contact> findById(Long id);

    List<Contact> findAll();

    Page<Contact> findAll(BooleanBuilder specification, Pageable pageable);

    List<Contact> findAll(BooleanBuilder specification);

    void deleteById(Long id);

    Page<Contact> findByContractorId(Long contractorId, Pageable pageable);
}
