package de.adesso.softwarepico.messageHandling;

import de.adesso.softwarepico.messageHandling.message.*;
import org.json.JSONObject;

import static de.adesso.softwarepico.configuration.HardwarePicoUtils.hpIdFromUri;
import static de.adesso.softwarepico.configuration.HardwarePicoUtils.hpIpFromUri;

public class MessageFactory {

    public static Message fromJson(JSONObject jsonMessage){
        MessageType messageType = MessageType.valueOf(jsonMessage.getString("messageType").toUpperCase());
        return switch(messageType){
            case BIND -> {
                String uri = jsonMessage.getString("hardwarePicoUri");
                int picoId = hpIdFromUri(uri);
                String picoIp = hpIpFromUri(uri);
                yield new BindMessage(picoId, picoIp);
            }
            case REBIND -> {
                String newPicoIp = hpIpFromUri(jsonMessage.getString("hardwarePicoUri"));
                yield new RebindMessage(newPicoIp);
            }
            case INFO -> new InfoMessage();

            case SENSOR_INFO -> {
                String status = jsonMessage.getString("status");
                yield new SensorInfoMessage(status);
            }
            case HEARTBEAT -> {
                int important = jsonMessage.getInt("important");
                yield new HeartBeatMessage(important);
            }
            case GET_STATUS -> {
                String sourceTopic = jsonMessage.getString("source");
                String messageId = jsonMessage.getString("messageId");
                yield new GetStatusMessage(sourceTopic, messageId);
            }
            case RESERVE -> {
                boolean reserve = jsonMessage.getBoolean("reserve");
                yield new ReservationMessage(reserve);
            }
        };
    }

}
