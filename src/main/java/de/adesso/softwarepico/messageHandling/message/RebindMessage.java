package de.adesso.softwarepico.messageHandling.message;

import de.adesso.softwarepico.messageHandling.MessageType;

public record RebindMessage(String hardwarePicoIp) implements Message {

    @Override
    public MessageType getMessageType() {
        return MessageType.REBIND;
    }
}
