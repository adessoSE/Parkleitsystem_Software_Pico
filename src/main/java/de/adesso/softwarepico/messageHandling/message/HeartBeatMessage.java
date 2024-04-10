package de.adesso.softwarepico.messageHandling.message;

import de.adesso.softwarepico.messageHandling.MessageType;

public record HeartBeatMessage(int importantFlag) implements Message {

    @Override
    public MessageType getMessageType() {
        return MessageType.HEARTBEAT;
    }
}
