package de.adesso.softwarepico.messageHandling.message;

import de.adesso.softwarepico.messageHandling.MessageType;

public record GetStatusMessage(String sourceTopic, String messageId) implements Message {

    @Override
    public MessageType getMessageType() {
        return MessageType.GET_STATUS;
    }
}