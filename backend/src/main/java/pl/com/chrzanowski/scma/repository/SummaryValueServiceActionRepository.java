package pl.com.chrzanowski.scma.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import pl.com.chrzanowski.scma.service.dto.SummaryValueServiceActionDTO;

import java.math.BigDecimal;

@Repository
public class SummaryValueServiceActionRepository {

    @PersistenceContext
    private EntityManager em;

    public SummaryValueServiceActionDTO getSummaryValues() {
        Query query = em.createQuery("SELECT SUM(sa.grossValue) FROM ServiceAction sa");
        BigDecimal grossValue = (BigDecimal) query.getSingleResult();

        query = em.createQuery("SELECT SUM(sa.netValue) FROM ServiceAction sa");
        BigDecimal netValue = (BigDecimal) query.getSingleResult();

        query = em.createQuery("SELECT SUM(sa.taxValue)  FROM ServiceAction sa");
        BigDecimal taxValue = (BigDecimal) query.getSingleResult();

        return SummaryValueServiceActionDTO.builder().summaryGrossValue(grossValue)
                .summaryNetValue(netValue)
                .summaryTaxValue(taxValue).build();
    }
}
