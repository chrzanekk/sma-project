package pl.com.chrzanowski.sma.common.config;

import com.blazebit.persistence.Criteria;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BlazePersistenceConfig {
    @Bean
    public CriteriaBuilderFactory cbf(EntityManagerFactory emf) {
        return Criteria.getDefault().createCriteriaBuilderFactory(emf);
    }

    @Bean
    public BlazeJPAQueryFactory blazeJPAQueryFactory(EntityManager em,
                                                     CriteriaBuilderFactory cbf) {
        return new BlazeJPAQueryFactory(em, cbf);
    }
}
