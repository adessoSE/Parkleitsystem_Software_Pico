package de.adesso.softwarepico.service;

import de.adesso.softwarepico.SoftwarePicoApplication;
import de.adesso.softwarepico.communication.Receiver;
import de.adesso.softwarepico.messageHandling.MessageFactory;
import de.adesso.softwarepico.messageHandling.handler.MessageHandler;
import de.adesso.softwarepico.messageHandling.message.Message;
import jakarta.annotation.PostConstruct;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

import static de.adesso.softwarepico.SoftwarePicoApplication.getUuid;

@Service
public class MessageService {
    private final Receiver receiver;
    private final List<MessageHandler> messageHandlers;

    @Autowired
    public MessageService(@Qualifier("universalReceiver") Receiver receiver, List<MessageHandler> messageHandlers) {
        this.receiver = receiver;
        this.messageHandlers = messageHandlers;
    }

    @PostConstruct
    public void subscribeToInbound(){
        receiver.subscribe(getUuid(), this::handle);
        receiver.subscribe("software-pico",  this::handle);
    }

    public void handle(JSONObject jsonMessage){
        Message message = MessageFactory.fromJson(jsonMessage);
        MessageHandler handler = findSupportingMessageHandler(message);
        handler.handle(message);
    }

    MessageHandler findSupportingMessageHandler(Message message){
        return messageHandlers.stream().filter(messageHandler -> messageHandler.supports(message)).findFirst().orElseThrow();
    }
}
