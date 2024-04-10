package de.adesso.softwarepico.messageHandling.handler;

import de.adesso.softwarepico.messageHandling.message.Message;

public interface MessageHandler {
    <T extends Message> void handle(T message);

    <T extends Message> boolean supports(T message);

}
