package pl.com.chrzanowski.sma.common.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

    public static final String GLOBAL_UNITS_CACHE = "globalUnits";
    public static final String GLOBAL_UNIT_BY_SYMBOL_CACHE = "globalUnitBySymbol";
    public static final String GLOBAL_UNITS_BY_TYPE_CACHE = "globalUnitsByType";
    public static final String UNIT_BY_ID_CACHE = "unitById";

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(
                GLOBAL_UNITS_CACHE,
                GLOBAL_UNIT_BY_SYMBOL_CACHE,
                GLOBAL_UNITS_BY_TYPE_CACHE,
                UNIT_BY_ID_CACHE
        );
    }
}
