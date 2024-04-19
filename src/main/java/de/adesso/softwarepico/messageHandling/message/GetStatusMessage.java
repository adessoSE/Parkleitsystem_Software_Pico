package de.adesso.softwarepico.messageHandling.message;

import de.adesso.communication.messageHandling.message.RequestMessage;
import de.adesso.softwarepico.messageHandling.SoftwarePicoMessageType;

import java.util.UUID;

public class GetStatusMessage extends RequestMessage {


    public GetStatusMessage(UUID requestId, String source) {
        super(requestId, source);
    }

    @Override
    public String getMessageType() {
        return SoftwarePicoMessageType.GET_STATUS.name();
    }

}
