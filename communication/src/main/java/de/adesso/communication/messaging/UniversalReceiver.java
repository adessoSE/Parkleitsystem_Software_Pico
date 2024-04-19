package de.adesso.communication.messaging;

import de.adesso.communication.messageHandling.MessageService;
import de.adesso.communication.messaging.Receiver;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

@Service
public class UniversalReceiver {

    private final List<Receiver> receivers;
    private final MessageService messageService;

    @Autowired
    public UniversalReceiver(List<Receiver> receivers, MessageService messageService) {
        this.receivers = receivers;
        this.messageService = messageService;
    }

    public void subscribe(String uri) {
       for(Receiver r : receivers){
           r.subscribe(uri, messageService::handle);
       }
    }
}
