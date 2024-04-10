package de.adesso.softwarepico.messageHandling.message;

import de.adesso.softwarepico.messageHandling.MessageType;

public class InfoMessage implements Message{

    @Override
    public MessageType getMessageType() {
        return MessageType.INFO;
    }
}
