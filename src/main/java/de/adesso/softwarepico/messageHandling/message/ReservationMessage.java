package de.adesso.softwarepico.messageHandling.message;

import de.adesso.softwarepico.messageHandling.MessageType;

public record ReservationMessage(boolean reserve) implements Message {

    @Override
    public MessageType getMessageType() {
        return MessageType.RESERVE;
    }
}
