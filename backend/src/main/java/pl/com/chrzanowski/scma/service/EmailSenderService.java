package pl.com.chrzanowski.scma.service;

import pl.com.chrzanowski.scma.service.dto.SentEmailDTO;

public interface EmailSenderService {

    void sendEmail(SentEmailDTO sentEmailDTO);
}
