package de.adesso.softwarepico.messageHandling.message;

import de.adesso.softwarepico.messageHandling.MessageType;

public record BindMessage(int hardwarePicoId, String hardwarePicoIp) implements Message {

    @Override
    public MessageType getMessageType() {
        return MessageType.BIND;
    }
}
