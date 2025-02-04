package pl.com.chrzanowski.sma.contact.service;

import pl.com.chrzanowski.sma.contact.dto.ContactDTO;

import java.util.List;

public interface ContactService {

    ContactDTO save(ContactDTO contactDTO);

    ContactDTO update(ContactDTO contactDTO);

    ContactDTO findById(Long id);

    List<ContactDTO> findAll();

    void delete(Long id);
}
