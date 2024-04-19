package de.adesso.softwarepico.messageHandling;

import de.adesso.communication.messageHandling.Message;
import de.adesso.communication.messageHandling.MessageFactory;
import de.adesso.communication.messageHandling.error.JsonMessageNotSupportedException;
import de.adesso.softwarepico.messageHandling.message.*;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;

import static de.adesso.softwarepico.configuration.HardwarePicoUtils.hpIdFromUri;
import static de.adesso.softwarepico.configuration.HardwarePicoUtils.hpIpFromUri;

@Service
public class SoftwarePicoMessageFactory implements MessageFactory {

    @Override
    public Message fromJson(JSONObject jsonMessage) throws JsonMessageNotSupportedException {
        SoftwarePicoMessageType softwarePicoMessageType = SoftwarePicoMessageType.valueOf(jsonMessage.getString("messageType").toUpperCase());
        return switch(softwarePicoMessageType){
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
                String messageId = jsonMessage.getString("requestId");
                yield new GetStatusMessage(UUID.fromString(messageId), sourceTopic);
            }
            case RESERVE -> {
                boolean reserve = jsonMessage.getBoolean("reserve");
                yield new ReservationMessage(reserve);
            }
        };
    }

    @Override
    public boolean supports(JSONObject jsonMessage) {
        try {
            return jsonMessage.has("messageType") &&
                    Arrays.stream(SoftwarePicoMessageType.values()).toList().contains(
                    SoftwarePicoMessageType.valueOf(jsonMessage.getString("messageType").toUpperCase()));
        }
        catch (IllegalArgumentException e){
            return false;
        }

    }
}
