package de.adesso.softwarepico.messageHandling.handler;

import de.adesso.softwarepico.service.mirror.MirrorService;
import de.adesso.softwarepico.service.SendingService;
import de.adesso.softwarepico.messageHandling.MessageType;
import de.adesso.softwarepico.messageHandling.message.GetStatusMessage;
import de.adesso.softwarepico.messageHandling.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetStatusHandler implements MessageHandler{

    private final MirrorService mirrorService;
    private final SendingService sendingService;

    @Autowired
    public GetStatusHandler(MirrorService mirrorService, SendingService sendingService) {
        this.mirrorService = mirrorService;
        this.sendingService = sendingService;
    }


    @Override
    public <T extends Message> void handle(T message) {
        if(supports(message)){
            GetStatusMessage getStatusMessage = (GetStatusMessage) message;
            String status = mirrorService.getSensorStatus().name();
            sendingService.sendResponse(getStatusMessage.sourceTopic(), getStatusMessage.messageId(), status);
        }
    }

    @Override
    public <T extends Message> boolean supports(T message) {
        return message.getMessageType().equals(MessageType.GET_STATUS);
    }
}
