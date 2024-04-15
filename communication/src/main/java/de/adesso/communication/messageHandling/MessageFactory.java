package de.adesso.communication.messageHandling;

import de.adesso.communication.messageHandling.error.JsonMessageNotSupportedException;
import org.json.JSONObject;


public interface MessageFactory {

    Message fromJson(JSONObject jsonMessage);
    boolean supports(JSONObject jsonMessage) throws JsonMessageNotSupportedException;

    default boolean isDefault(){
        return false;
    }
}
