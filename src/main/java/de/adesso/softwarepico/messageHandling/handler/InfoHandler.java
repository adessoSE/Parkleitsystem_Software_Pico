package de.adesso.softwarepico.messageHandling.handler;

import de.adesso.softwarepico.service.mirror.MirrorService;
import de.adesso.softwarepico.messageHandling.MessageType;
import de.adesso.softwarepico.messageHandling.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InfoHandler implements MessageHandler{

    private final MirrorService mirrorService;

    @Autowired
    public InfoHandler(MirrorService mirrorService) {
        this.mirrorService = mirrorService;
    }

    @Override
    public <T extends Message> void handle(T message) {
        if(supports(message)){
            mirrorService.testConnection();
        }
    }

    @Override
    public <T extends Message> boolean supports(T message) {
        return message.getMessageType().equals(MessageType.INFO);
    }
}
