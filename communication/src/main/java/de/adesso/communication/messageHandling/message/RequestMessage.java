package de.adesso.communication.messageHandling.message;

import de.adesso.communication.messageHandling.Message;

import java.util.UUID;

public abstract class RequestMessage implements Message {

    private final UUID requestId;
    private final String source;

    protected RequestMessage(UUID requestId, String source) {
        this.requestId = requestId;
        this.source = source;
    }

    public UUID getRequestId() {
        return requestId;
    }

    public String getSource() {
        return source;
    }
}
