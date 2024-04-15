package de.adesso.softwarepico.messageHandling.handler;

import de.adesso.communication.messageHandling.MessageHandler;
import de.adesso.softwarepico.service.mirror.MirrorService;
import de.adesso.softwarepico.messageHandling.SoftwarePicoMessageType;
import de.adesso.softwarepico.messageHandling.message.BindMessage;
import de.adesso.communication.messageHandling.Message;
import org.springframework.stereotype.Service;

@Service
public class BindHandler implements MessageHandler {

    private final MirrorService mirrorService;

    public BindHandler(MirrorService mirrorService) {
        this.mirrorService = mirrorService;
    }

    @Override
    public <T extends Message> void handle(T message) {
        if(supports(message)){
            BindMessage bindMessage = (BindMessage) message;
            mirrorService.initialize(bindMessage.hardwarePicoId(), bindMessage.hardwarePicoIp());
        }
    }

    @Override
    public <T extends Message> boolean supports(T message) {
        return message.getMessageType().equals(SoftwarePicoMessageType.BIND.name());
    }
}
