package de.adesso.communication.messageHandling.answerChecking;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class AnswerCheckingConfiguration {

    @Bean
    public Map<UUID, Answer> pendingRequests(){
        return new ConcurrentHashMap<>();
    }

}
