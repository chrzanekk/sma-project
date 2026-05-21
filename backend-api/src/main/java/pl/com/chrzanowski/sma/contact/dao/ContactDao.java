package pl.com.chrzanowski.sma.contact.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.com.chrzanowski.sma.common.dao.BaseCrudDao;
import pl.com.chrzanowski.sma.contact.model.Contact;

import java.util.List;

public interface ContactDao extends BaseCrudDao<Contact, Long> {

    List<Contact> saveAll(List<Contact> contacts);

    List<Contact> findAll();

    Page<Contact> findByContractorId(Long contractorId, Pageable pageable);
}
