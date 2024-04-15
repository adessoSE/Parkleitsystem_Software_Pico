package de.adesso.communication.messageHandling.error;

import org.json.JSONObject;

public class JsonMessageNotSupportedException extends RuntimeException{

    public JsonMessageNotSupportedException(JSONObject failedMessage){
        super("Json Message not supported by Message Factory : " + failedMessage);
        this.failedMessage = failedMessage;
    }

    private final JSONObject failedMessage;

    public JSONObject getFailedMessage() {
        return failedMessage;
    }
}
