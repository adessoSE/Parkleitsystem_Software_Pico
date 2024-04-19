package de.adesso.softwarepico.messageHandling.handler;

import de.adesso.communication.messageHandling.MessageHandler;
import de.adesso.communication.messaging.UniversalSender;
import de.adesso.softwarepico.service.mirror.MirrorService;
import de.adesso.softwarepico.messageHandling.SoftwarePicoMessageType;
import de.adesso.communication.messageHandling.Message;
import de.adesso.softwarepico.messageHandling.message.RebindMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RebindHandler implements MessageHandler {

    private final MirrorService mirrorService;
    private final UniversalSender universalSender;

    @Autowired
    public RebindHandler(MirrorService mirrorService, UniversalSender universalSender) {
        this.mirrorService = mirrorService;
        this.universalSender = universalSender;
    }

    @Override
    public <T extends Message> void handle(T message) {
        if(supports(message)){
            RebindMessage rebindMessage = (RebindMessage) message;
            universalSender.clearDnsCache();
            mirrorService.rebind(rebindMessage.hardwarePicoIp());
        }
    }

    @Override
    public <T extends Message> boolean supports(T message) {
        return message.getMessageType().equals(SoftwarePicoMessageType.REBIND.name());
    }
}
