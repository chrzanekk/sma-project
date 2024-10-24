package pl.com.chrzanowski.sma.email.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import pl.com.chrzanowski.sma.email.model.SendEmail;
import pl.com.chrzanowski.sma.email.repository.SendEmailRepository;

@Repository("jpa")
public class SendEmailJPADaoImpl implements SendEmailDao {

    private final Logger log = LoggerFactory.getLogger(SendEmailJPADaoImpl.class);

    private final SendEmailRepository sendEmailRepository;

    public SendEmailJPADaoImpl(SendEmailRepository sendEmailRepository) {
        this.sendEmailRepository = sendEmailRepository;
    }

    @Override
    public void save(SendEmail sendEmail) {
        log.debug("JPA DAO: save sendEmail");
        sendEmailRepository.save(sendEmail);
    }
}
