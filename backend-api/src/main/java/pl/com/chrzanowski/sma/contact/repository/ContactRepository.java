package pl.com.chrzanowski.sma.contact.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import pl.com.chrzanowski.sma.contact.model.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long>,
        JpaSpecificationExecutor<Contact>,
        QuerydslPredicateExecutor<Contact> {

    void deleteById(Long id);
}
