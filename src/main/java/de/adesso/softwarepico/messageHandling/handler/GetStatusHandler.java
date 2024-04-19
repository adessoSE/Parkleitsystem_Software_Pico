package de.adesso.softwarepico.messageHandling.handler;

import de.adesso.communication.messageHandling.handler.RequestMessageHandler;
import de.adesso.communication.messaging.UniversalSender;
import de.adesso.softwarepico.service.mirror.MirrorService;
import de.adesso.softwarepico.messageHandling.SoftwarePicoMessageType;
import de.adesso.communication.messageHandling.Message;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetStatusHandler extends RequestMessageHandler {

    private final MirrorService mirrorService;

    @Autowired
    public GetStatusHandler(MirrorService mirrorService, UniversalSender universalSender) {
        super(universalSender);
        this.mirrorService = mirrorService;
    }

    @Override
    protected <T extends Message> JSONObject buildResponseMessage(T message) {
        String status = mirrorService.getSensorStatus().name();
        return new JSONObject().put("messageType", "status_response").put("status", status);
    }

    @Override
    public <T extends Message> boolean supports(T message) {
        return message.getMessageType().equals(SoftwarePicoMessageType.GET_STATUS.name());
    }
}
