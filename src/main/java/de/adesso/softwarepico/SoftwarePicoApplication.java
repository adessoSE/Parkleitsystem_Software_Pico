package de.adesso.softwarepico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@SpringBootApplication
@EnableScheduling
@ComponentScan("de.adesso")
public class SoftwarePicoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoftwarePicoApplication.class, args);
    }

    private static final String uuid = UUID.randomUUID().toString();

    public static String getUuid(){
        return uuid;
    }

    @Bean
    public String uuid(){
        return uuid;
    }

    @Bean
    public CompletableFuture<Void> synchronizer(){
        return new CompletableFuture<>();
    }

}
