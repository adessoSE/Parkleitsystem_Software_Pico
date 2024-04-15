package de.adesso.communication.messaging;

import de.adesso.communication.messaging.UniversalReceiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UniversalSubscriber {

    private final UniversalReceiver universalReceiver;

    @Autowired
    public UniversalSubscriber(UniversalReceiver universalReceiver) {
        this.universalReceiver = universalReceiver;
    }

    public void subscribe(String uri){
        universalReceiver.subscribe(uri);
    }
}
