package de.adesso.communication.messageHandling.handler;

import de.adesso.communication.messageHandling.Message;
import de.adesso.communication.messageHandling.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DefaultMessageHandler implements MessageHandler {

    private final Logger logger = LoggerFactory.getLogger(DefaultMessageHandler.class);

    @Override
    public <T extends Message> void handle(T message) {
        logger.info("[Received message: " + message.toString() + "]");
    }

    @Override
    public <T extends Message> boolean supports(T message) {
        return true;
    }

    @Override
    public boolean isDefault() {
        return true;
    }
}
