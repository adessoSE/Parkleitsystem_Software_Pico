package de.adesso.softwarepico.messageHandling.handler;

import de.adesso.softwarepico.service.mirror.MirrorService;
import de.adesso.softwarepico.configuration.SensorStatus;
import de.adesso.softwarepico.messageHandling.MessageType;
import de.adesso.softwarepico.messageHandling.message.Message;
import de.adesso.softwarepico.messageHandling.message.SensorInfoMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SensorInfoHandler implements MessageHandler{

    private final MirrorService mirrorService;

    @Autowired
    public SensorInfoHandler(MirrorService mirrorService) {
        this.mirrorService = mirrorService;
    }

    @Override
    public <T extends Message> void handle(T message) {
        if(supports(message)){
            SensorInfoMessage sensorInfoMessage = (SensorInfoMessage) message;
            mirrorService.setSensorStatus(SensorStatus.valueOf(sensorInfoMessage.status().toUpperCase()));
        }
    }

    @Override
    public <T extends Message> boolean supports(T message) {
        return message.getMessageType().equals(MessageType.SENSOR_INFO);
    }
}
