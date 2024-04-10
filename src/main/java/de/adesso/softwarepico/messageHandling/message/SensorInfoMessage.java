package de.adesso.softwarepico.messageHandling.message;

import de.adesso.softwarepico.messageHandling.MessageType;

public record SensorInfoMessage(String status) implements Message {

    @Override
    public MessageType getMessageType() {
        return MessageType.SENSOR_INFO;
    }
}
