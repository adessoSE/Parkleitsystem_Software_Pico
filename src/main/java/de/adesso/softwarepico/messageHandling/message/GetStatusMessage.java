package de.adesso.softwarepico.messageHandling.message;

import de.adesso.communication.messageHandling.Message;
import de.adesso.softwarepico.messageHandling.SoftwarePicoMessageType;

public record GetStatusMessage(String sourceTopic, String messageId) implements Message {

    @Override
    public String getMessageType() {
        return SoftwarePicoMessageType.GET_STATUS.name();
    }
}
