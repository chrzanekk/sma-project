package pl.com.chrzanowski.scma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.com.chrzanowski.scma.domain.SentEmail;

@Repository
public interface SentEmailRepository extends JpaRepository<SentEmail, Long> {
}
