package pl.com.chrzanowski.sma.contact.service;

import pl.com.chrzanowski.sma.contact.dto.ContactDTO;

import java.util.List;

public interface ContactService {

    ContactDTO save(ContactDTO contactBaseDTO);

    ContactDTO update(ContactDTO contactBaseDTO);

    ContactDTO findById(Long id);

    List<ContactDTO> findAll();

    void delete(Long id);
}
