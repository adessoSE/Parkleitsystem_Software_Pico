package de.adesso.communication.messageHandling.message;

import de.adesso.communication.messageHandling.Message;
import de.adesso.communication.messageHandling.type.GenericMessageType;

public record GenericMessage(String jsonString) implements Message {
    @Override
    public String getMessageType() {
        return GenericMessageType.GENERIC.name();
    }

    @Override
    public String toString() {
        return jsonString;
    }
}
