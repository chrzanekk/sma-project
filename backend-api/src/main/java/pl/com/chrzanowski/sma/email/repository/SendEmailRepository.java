package pl.com.chrzanowski.sma.email.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.com.chrzanowski.sma.email.model.SendEmail;


public interface SendEmailRepository extends JpaRepository<SendEmail, Long> {
}
