package de.adesso.communication.addressResolution;

import de.adesso.communication.cloud.CloudSender;
import de.adesso.communication.hardware.HardwareSender;
import de.adesso.communication.messaging.Sender;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class DnsService {


    private final Map<String, CompletableFuture<String>> pendingAnswers;
    private final DnsCache cache;
    private final DnsSendingService sendingService;

    @Autowired
    public DnsService(Map<String, CompletableFuture<String>> pendingAnswers, DnsCache cache, DnsSendingService sendingService) {
        this.pendingAnswers = pendingAnswers;
        this.cache = cache;
        this.sendingService = sendingService;
    }

    public CompletableFuture<String> getTopicForService(String service){
        if(cache.hasEntryFor(service)){
            CompletableFuture<String> c = new CompletableFuture<>();
            c.complete(cache.getAddress(service));
            return c;
        }
        else {
            CompletableFuture<String> c = getTopicAsync(service);
            c.thenAccept(domainAndTopic -> cache.addEntry(service, domainAndTopic));
            return c;
        }
    }

    protected CompletableFuture<String> getTopicAsync(String service){
        String messageId = UUID.randomUUID().toString();
        CompletableFuture<String> c = new CompletableFuture<>();
        pendingAnswers.put(messageId, c);
        sendingService.dnsRequest(messageId, service);
        return c;
    }

}
