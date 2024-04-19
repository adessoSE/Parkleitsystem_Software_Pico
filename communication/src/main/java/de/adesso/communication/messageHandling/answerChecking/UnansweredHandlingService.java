package de.adesso.communication.messageHandling.answerChecking;

import de.adesso.communication.messageHandling.MessageErrorHandler;
import de.adesso.communication.messageHandling.error.DidNotRespondException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UnansweredHandlingService {

    private final Map<UUID, Answer> pendingAnswers;
    private final List<MessageErrorHandler> messageErrorHandlers;

    @Autowired
    public UnansweredHandlingService(Map<UUID, Answer> pendingAnswers, List<MessageErrorHandler> messageErrorHandlers) {
        this.pendingAnswers = pendingAnswers;
        this.messageErrorHandlers = messageErrorHandlers;
    }

    @Scheduled(fixedRate = 10000L)
    public void checkForUnanswered(){
        for(UUID id : pendingAnswers.keySet()){
            Answer a = pendingAnswers.get(id);
            if(a.getSendTime().plus(a.getTimeout()).isBefore(LocalDateTime.now())){
                pendingAnswers.remove(id);
                DidNotRespondException didNotRespondException = new DidNotRespondException(a);
                MessageErrorHandler meh = findSupportingMessageErrorHandler(didNotRespondException);
                meh.handle(didNotRespondException);
            }
        }
    }

    protected MessageErrorHandler findSupportingMessageErrorHandler(Exception e){
        return messageErrorHandlers.stream().filter(messageErrorHandler -> messageErrorHandler.supports(e) && !messageErrorHandler.isDefault()).findFirst()
                .orElse(messageErrorHandlers.stream().filter(messageErrorHandler -> messageErrorHandler.supports(e)).findFirst().orElseThrow());
    }
}
