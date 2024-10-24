package pl.com.chrzanowski.sma.email.service;

import pl.com.chrzanowski.sma.email.dto.SentEmailDTO;

public interface EmailSenderService {

    void sendEmail(SentEmailDTO sentEmailDTO);
}
