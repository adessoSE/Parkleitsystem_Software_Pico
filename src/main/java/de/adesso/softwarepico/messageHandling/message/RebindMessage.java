package de.adesso.softwarepico.messageHandling.message;

import de.adesso.communication.messageHandling.Message;
import de.adesso.softwarepico.messageHandling.SoftwarePicoMessageType;

public record RebindMessage(String hardwarePicoIp) implements Message {

    @Override
    public String getMessageType() {
        return SoftwarePicoMessageType.REBIND.name();
    }
}
