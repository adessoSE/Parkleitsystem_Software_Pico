package de.adesso.softwarepico.messageHandling.message;

import de.adesso.communication.messageHandling.Message;
import de.adesso.softwarepico.messageHandling.SoftwarePicoMessageType;

public record SensorInfoMessage(String status) implements Message {

    @Override
    public String getMessageType() {
        return SoftwarePicoMessageType.SENSOR_INFO.name();
    }
}
