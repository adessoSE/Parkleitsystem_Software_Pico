package de.adesso.communication.messageHandling.factory;

import de.adesso.communication.messageHandling.Message;
import de.adesso.communication.messageHandling.MessageFactory;
import de.adesso.communication.messageHandling.message.GenericMessage;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class DefaultMessageFactory implements MessageFactory {
    @Override
    public Message fromJson(JSONObject jsonMessage) {
        return new GenericMessage(jsonMessage.toString());
    }

    @Override
    public boolean supports(JSONObject jsonMessage) {
        return true;
    }

    @Override
    public boolean isDefault() {
        return true;
    }
}
