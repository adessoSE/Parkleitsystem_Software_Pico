package de.adesso.communication.messageHandling;

import de.adesso.communication.messageHandling.error.AnswerAfterTimeoutException;
import de.adesso.communication.messageHandling.answerChecking.Answer;
import de.adesso.communication.messageHandling.answerChecking.AnswerCheckingService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class MessageService {

    private final List<MessageHandler> messageHandlers;
    private final List<MessageFactory> messageFactories;
    private final List<MessageErrorHandler> messageErrorHandlers;
    private final AnswerCheckingService answerCheckingService;

    @Autowired
    public MessageService(List<MessageHandler> messageHandlers, List<MessageFactory> messageFactories, List<MessageErrorHandler> messageErrorHandlers, AnswerCheckingService answerCheckingService) {
        this.messageHandlers = messageHandlers;
        this.messageFactories = messageFactories;
        this.messageErrorHandlers = messageErrorHandlers;
        this.answerCheckingService = answerCheckingService;
    }

    public void handle(JSONObject jsonMessage){
        try {
            checkPendingAnswers(jsonMessage);
            MessageFactory messageFactory = findSupportingMessageFactory(jsonMessage);
            Message message = messageFactory.fromJson(jsonMessage);
            handle(message);
        }
        catch (Exception e){
            MessageErrorHandler errorHandler = findSupportingMessageErrorHandler(e);
            errorHandler.handle(e);
        }

    }

    protected void handle(Message message){
        MessageHandler messageHandler = findSupportingMessageHandler(message);
        messageHandler.handle(message);
    }

    protected MessageFactory findSupportingMessageFactory(JSONObject jsonMessage){
        return messageFactories.stream().filter(messageFactory -> messageFactory.supports(jsonMessage) && !messageFactory.isDefault()).findFirst()
                .orElse(messageFactories.stream().filter(messageFactory -> messageFactory.supports(jsonMessage)).findFirst().orElseThrow());
    }

    protected MessageHandler findSupportingMessageHandler(Message message){
        return messageHandlers.stream().filter(messageHandler -> messageHandler.supports(message) && !messageHandler.isDefault()).findFirst()
                .orElse(messageHandlers.stream().filter(messageHandler -> messageHandler.supports(message)).findFirst().orElseThrow());
    }

    protected MessageErrorHandler findSupportingMessageErrorHandler(Exception e){
        return messageErrorHandlers.stream().filter(messageErrorHandler -> messageErrorHandler.supports(e) && !messageErrorHandler.isDefault()).findFirst()
                .orElse(messageErrorHandlers.stream().filter(messageErrorHandler -> messageErrorHandler.supports(e)).findFirst().orElseThrow());
    }

    protected void checkPendingAnswers(JSONObject jsonMessage) throws AnswerAfterTimeoutException{
        if(jsonMessage.has("context") && jsonMessage.getBoolean("context")){
            UUID responseId = UUID.fromString(jsonMessage.getString("responseId"));
            Answer answer = answerCheckingService.getAnswer(responseId);
            if(answer.getSendTime().plus(answer.getTimeout()).isBefore(LocalDateTime.now())) throw new AnswerAfterTimeoutException(answer);
            answerCheckingService.setFulfilled(responseId);
        }
    }
}
