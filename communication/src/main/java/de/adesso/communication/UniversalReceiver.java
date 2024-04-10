package de.adesso.communication;

import de.adesso.communication.Receiver;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

@Service
public class UniversalReceiver implements Receiver {

    private final List<Receiver> receivers;

    @Autowired
    public UniversalReceiver(List<Receiver> receivers) {
        this.receivers = receivers;
    }

    @Override
    public void subscribe(String uri, Consumer<JSONObject> jsonConsumer) {
       for(Receiver r : receivers){
           r.subscribe(uri, jsonConsumer);
       }
    }
}
