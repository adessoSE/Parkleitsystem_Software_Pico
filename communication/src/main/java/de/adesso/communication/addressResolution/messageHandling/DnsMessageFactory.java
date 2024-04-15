package de.adesso.communication.addressResolution.messageHandling;

import de.adesso.communication.messageHandling.Message;
import de.adesso.communication.messageHandling.MessageFactory;
import de.adesso.communication.messageHandling.error.JsonMessageNotSupportedException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class DnsMessageFactory implements MessageFactory {
    @Override
    public Message fromJson(JSONObject jsonMessage) throws JsonMessageNotSupportedException {
        if(supports(jsonMessage)) {
            DnsMessageType messageType = DnsMessageType.valueOf(jsonMessage.getString("messageType").toUpperCase());
            return switch (messageType) {
                case DNS_RESPONSE -> {
                    String messageId = jsonMessage.getString("messageId");
                    String request = jsonMessage.getString("request");
                    String response = jsonMessage.getString("response");
                    String domain = jsonMessage.getString("domain");
                    yield new DnsMessage(messageId, request, response, domain);
                }
            };
        }
        throw new JsonMessageNotSupportedException(jsonMessage);
    }

    @Override
    public boolean supports(JSONObject jsonMessage) {
        try {
            return jsonMessage.has("messageType") &&
                    Arrays.stream(DnsMessageType.values()).toList().contains(
                            DnsMessageType.valueOf(jsonMessage.getString("messageType").toUpperCase()));
        }
        catch (IllegalArgumentException e){
            return false;
        }
    }
}
