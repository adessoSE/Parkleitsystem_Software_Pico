package de.adesso.softwarepico.messageHandling.message;

import de.adesso.communication.messageHandling.Message;
import de.adesso.softwarepico.messageHandling.SoftwarePicoMessageType;

public record BindMessage(int hardwarePicoId, String hardwarePicoIp) implements Message {

    @Override
    public String getMessageType() {
        return SoftwarePicoMessageType.BIND.name();
    }
}
