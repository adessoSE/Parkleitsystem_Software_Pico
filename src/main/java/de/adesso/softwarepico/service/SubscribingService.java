package de.adesso.softwarepico.service;

import de.adesso.communication.messaging.UniversalReceiver;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

import static de.adesso.softwarepico.SoftwarePicoApplication.getUuid;

@Service
public class SubscribingService {

    private final UniversalReceiver universalReceiver;
    private final CompletableFuture<Void> synchronizer;

    @Autowired
    public SubscribingService(UniversalReceiver universalReceiver, CompletableFuture<Void> synchronizer) {
        this.universalReceiver = universalReceiver;
        this.synchronizer = synchronizer;
    }

    @PostConstruct
    public void subscribeToInbound(){
        universalReceiver.subscribe(getUuid());
        universalReceiver.subscribe("software-pico");

        synchronizer.complete(null);
    }

}
