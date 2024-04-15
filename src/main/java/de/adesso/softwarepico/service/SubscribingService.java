package de.adesso.softwarepico.service;

import de.adesso.communication.messaging.UniversalSubscriber;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

import static de.adesso.softwarepico.SoftwarePicoApplication.getUuid;

@Service
public class SubscribingService {

    private final UniversalSubscriber universalSubscriber;
    private final CompletableFuture<Void> synchronizer;

    @Autowired
    public SubscribingService(UniversalSubscriber universalSubscriber, CompletableFuture<Void> synchronizer) {
        this.universalSubscriber = universalSubscriber;
        this.synchronizer = synchronizer;
    }

    @PostConstruct
    public void subscribeToInbound(){
        universalSubscriber.subscribe(getUuid());
        universalSubscriber.subscribe("software-pico");

        synchronizer.complete(null);
    }

}
