package de.adesso.communication.addressResolution.messageHandling;

import de.adesso.communication.messageHandling.Message;
import de.adesso.communication.messageHandling.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class DnsMessageHandler implements MessageHandler {

    private final Map<String, CompletableFuture<String>> pendingAnswers;
    private final Logger logger = LoggerFactory.getLogger(DnsMessageHandler.class);

    @Autowired
    public DnsMessageHandler(Map<String, CompletableFuture<String>> pendingAnswers) {
        this.pendingAnswers = pendingAnswers;
    }

    @Override
    public <T extends Message> void handle(T message) {
        if(supports(message)){
            DnsMessage dnsMessage = (DnsMessage) message;
            if(pendingAnswers.containsKey(dnsMessage.messageId())){
                CompletableFuture<String> response = pendingAnswers.get(dnsMessage.messageId());
                response.complete(dnsMessage.domain() + "/" + dnsMessage.response());
                pendingAnswers.remove(dnsMessage.messageId());
            }
            else {
                logger.warn("[Received unexpected DNS message]");
            }
        }
    }

    @Override
    public <T extends Message> boolean supports(T message) {
        return message.getMessageType().equals(DnsMessageType.DNS_RESPONSE.name());
    }
}
