package de.adesso.communication.messaging;

import de.adesso.communication.addressResolution.DnsService;
import de.adesso.communication.messageHandling.answerChecking.Answer;
import de.adesso.communication.messageHandling.answerChecking.AnswerCheckingService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class UniversalSender{
    private final DnsService dnsService;
    private final AnswerCheckingService answerCheckingService;
    private final String uuid;
    private final List<Sender> senders;

    @Autowired
    public UniversalSender(DnsService dnsService, AnswerCheckingService answerCheckingService, String uuid, List<Sender> senders) {
        this.dnsService = dnsService;
        this.answerCheckingService = answerCheckingService;
        this.uuid = uuid;
        this.senders = senders;
    }

    public void send(String uri, JSONObject j) {
        CompletableFuture<String> c = dnsService.getTopicForService(uri);
        c.thenAccept(domainAndAddress -> {
            sendToDomain(domainAndAddress, j);
        });
    }

    public void sendExpectAnswer(String uri, JSONObject j){
        UUID requestId = answerCheckingService.addPendingAnswer(uri, j);
        j.put("context", true);
        j.put("requestId", requestId.toString());
        send(uri, j);
    }

    public void sendExpectAnswer(String uri, JSONObject j, Long timeoutMillis){
        UUID requestId = answerCheckingService.addPendingAnswer(new Answer(uri, j, timeoutMillis));
        j.put("context", false);
        j.put("source", uuid);
        j.put("requestId", requestId.toString());
        send(uri, j);
    }

    protected void sendToDomain(String domainAndAddress, JSONObject jsonMessage){
        String domain = domainAndAddress.split("/")[0];
        String address = domainAndAddress.split("/")[1];
        Sender sender = senders.stream().filter(s -> s.supports(domain)).findFirst().orElseThrow();
        sender.send(address, jsonMessage);
    }

    public void publishToDns(String service){
        JSONObject dnsPublishMessage = new JSONObject()
                .put("messageType", "dns_publish")
                .put("service", service)
                .put("topic", uuid);
        Sender sender = senders.stream().filter(s -> s.supports("mqtt")).findFirst().orElseThrow();
        sender.send("cloud-dns", dnsPublishMessage);
    }

    public void clearDnsCache(){
        dnsService.clearDnsCache();
    }

}
