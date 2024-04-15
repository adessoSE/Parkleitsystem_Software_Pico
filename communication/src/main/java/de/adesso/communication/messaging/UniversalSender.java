package de.adesso.communication.messaging;

import de.adesso.communication.addressResolution.DnsService;
import de.adesso.communication.cloud.CloudSender;
import de.adesso.communication.hardware.HardwareSender;
import de.adesso.communication.messageHandling.error.DidNotRespondException;
import de.adesso.communication.messageHandling.error.MessageErrorHandler;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Service
public class UniversalSender implements Sender{
    private final DnsService dnsService;
    private final String uuid;
    private final List<CloudSender> cloudSenders;
    private final List<HardwareSender> hardwareSenders;
    private final List<MessageErrorHandler> messageErrorHandlers;

    @Autowired
    public UniversalSender(DnsService dnsService, String uuid, List<CloudSender> cloudSenders, List<HardwareSender> hardwareSenders, @Lazy List<MessageErrorHandler> messageErrorHandlers) {
        this.dnsService = dnsService;
        this.uuid = uuid;
        this.cloudSenders = cloudSenders;
        this.hardwareSenders = hardwareSenders;
        this.messageErrorHandlers = messageErrorHandlers;
    }

    @Override
    public void send(String uri, JSONObject j) {
        CompletableFuture<String> c = dnsService.getTopicForService(uri);
        c.thenAccept(domainAndAddress -> {
            try {
                sendToDomain(domainAndAddress, j);
            }
            catch(DidNotRespondException e){
                messageErrorHandlers.stream().filter(messageErrorHandler -> messageErrorHandler.supports(e)).findFirst().ifPresent(messageErrorHandler -> messageErrorHandler.handle(e));
            }
        });
    }

    protected void sendToDomain(String domainAndAddress, JSONObject jsonMessage){
        String domain = domainAndAddress.split("/")[0];
        String address = domainAndAddress.split("/")[1];
        if(domain.equals("ip")){
            for(HardwareSender hardwareSender : hardwareSenders){
                hardwareSender.send(address, jsonMessage);
            }
        }
        else if(domain.equals("mqtt")){
            for (CloudSender cloudSender : cloudSenders){
                cloudSender.send(address, jsonMessage);
            }
        }
    }

    public void publishToDns(String service){
        JSONObject dnsPublishMessage = new JSONObject()
                .put("messageType", "dns_publish")
                .put("service", service)
                .put("topic", uuid);
        for(CloudSender cloudSender : cloudSenders){
            cloudSender.send("cloud-dns", dnsPublishMessage);
        }
    }

}
