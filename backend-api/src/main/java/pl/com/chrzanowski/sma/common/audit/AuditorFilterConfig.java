package pl.com.chrzanowski.sma.common.audit;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class AuditorFilterConfig {

    @Bean
    public FilterRegistrationBean<AuditorCacheClearingFilter> auditorCacheClearingFilter() {
        FilterRegistrationBean<AuditorCacheClearingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AuditorCacheClearingFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);
        return registrationBean;
    }
}
