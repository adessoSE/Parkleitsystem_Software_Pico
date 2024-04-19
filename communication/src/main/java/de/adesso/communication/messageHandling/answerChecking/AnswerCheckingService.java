package de.adesso.communication.messageHandling.answerChecking;

import de.adesso.communication.messageHandling.MessageErrorHandler;
import de.adesso.communication.messageHandling.error.DidNotRespondException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AnswerCheckingService {

    private final Map<UUID, Answer> pendingAnswers;

    @Autowired
    public AnswerCheckingService(Map<UUID, Answer> pendingAnswers) {
        this.pendingAnswers = pendingAnswers;
    }

    public UUID addPendingAnswer(Answer answer){
        UUID requestId = UUID.randomUUID();
        pendingAnswers.put(requestId, answer);
        return requestId;

    }

    public UUID addPendingAnswer(String destination, JSONObject jsonMessage){
        return addPendingAnswer(new Answer(destination, jsonMessage));
    }

    public Answer getAnswer(UUID id){
        return pendingAnswers.get(id);
    }

    public void setFulfilled(UUID id){
        pendingAnswers.remove(id);
    }

}
