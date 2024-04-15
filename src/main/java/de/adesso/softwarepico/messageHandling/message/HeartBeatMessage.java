package de.adesso.softwarepico.messageHandling.message;

import de.adesso.communication.messageHandling.Message;
import de.adesso.softwarepico.messageHandling.SoftwarePicoMessageType;

public record HeartBeatMessage(int importantFlag) implements Message {

    @Override
    public String getMessageType() {
        return SoftwarePicoMessageType.HEARTBEAT.name();
    }
}
