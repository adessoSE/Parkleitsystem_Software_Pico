package de.adesso.communication.addressResolution;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Configuration
@EnableScheduling
public class DnsConfiguration {
    @Bean
    public Map<String, CompletableFuture<String>> pendingAnswers(){
        return new HashMap<>();
    }

}
