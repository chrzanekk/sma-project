package pl.com.chrzanowski.sma.email.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.com.chrzanowski.sma.email.model.SendEmail;


public interface SendEmailRepository extends JpaRepository<SendEmail, Long> {
}
