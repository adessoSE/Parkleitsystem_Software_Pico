package de.adesso.communication.messageHandling.handler;

import de.adesso.communication.messageHandling.Message;
import de.adesso.communication.messageHandling.MessageHandler;
import de.adesso.communication.messageHandling.message.RequestMessage;
import de.adesso.communication.messaging.UniversalSender;
import org.json.JSONObject;

public abstract class RequestMessageHandler implements MessageHandler {

    private final UniversalSender universalSender;

    protected RequestMessageHandler(UniversalSender universalSender) {
        this.universalSender = universalSender;
    }

    protected abstract <T extends Message> JSONObject buildResponseMessage(T message);

    @Override
    public <T extends Message> void handle(T message){
        if(supports(message) && message instanceof RequestMessage requestMessage) {
            JSONObject jsonResponse = buildResponseMessage(message);
            jsonResponse.put("context", true);
            jsonResponse.put("responseId", requestMessage.getRequestId());
            universalSender.send(requestMessage.getSource(), jsonResponse);
        }
    }

}
