package vn.edu.iuh.fit.backEnd.configs;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimiterConfig {
    @Bean
    public RateLimiter loginCompanyLimiter(RateLimiterRegistry registry) {
        return registry.rateLimiter("loginCompanyLimiter");
    }
    @Bean
    public RateLimiter homeCompanyLimiter(RateLimiterRegistry registry) {
        return registry.rateLimiter("homeCompanyLimiter");
    }
}

