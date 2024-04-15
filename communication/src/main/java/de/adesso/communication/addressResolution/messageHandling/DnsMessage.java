package de.adesso.communication.addressResolution.messageHandling;

import de.adesso.communication.messageHandling.Message;

public record DnsMessage(String messageId, String request, String response, String domain) implements Message {
    @Override
    public String getMessageType() {
        return DnsMessageType.DNS_RESPONSE.name();
    }
}
