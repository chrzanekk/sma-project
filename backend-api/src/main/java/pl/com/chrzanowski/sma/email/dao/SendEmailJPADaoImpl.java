package pl.com.chrzanowski.sma.email.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import pl.com.chrzanowski.sma.email.model.SendEmail;
import pl.com.chrzanowski.sma.email.repository.SendEmailRepository;

@Repository("sendEmailJPA")
public class SendEmailJPADaoImpl implements SendEmailDao {

    private final Logger log = LoggerFactory.getLogger(SendEmailJPADaoImpl.class);

    private final SendEmailRepository sendEmailRepository;

    public SendEmailJPADaoImpl(SendEmailRepository sendEmailRepository) {
        this.sendEmailRepository = sendEmailRepository;
    }

    @Override
    public void save(SendEmail sendEmail) {
        log.debug("JPA DAO: save sent email.");
        sendEmailRepository.save(sendEmail);
    }

    @Override
    public void deleteByUserId(Long userId) {
        log.debug("JPA DAO: delete sent emails by userId.");
        sendEmailRepository.deleteByUserId(userId);
    }
}
