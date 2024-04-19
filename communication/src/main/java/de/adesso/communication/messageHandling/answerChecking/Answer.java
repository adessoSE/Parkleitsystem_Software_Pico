package de.adesso.communication.messageHandling.answerChecking;

import org.json.JSONObject;

import java.time.Duration;
import java.time.LocalDateTime;

public class Answer {
    private String destination;
    private Duration timeout;
    private final LocalDateTime sendTime;
    private final JSONObject jsonMessage;

    public Answer(String destination, JSONObject jsonMessage){
        this.jsonMessage = jsonMessage;
        this.sendTime = LocalDateTime.now();
    }

    public Answer(String destination, JSONObject jsonMessage, Duration timeout){
        this(destination, jsonMessage);
        this.timeout = timeout;
    }

    public Answer(String destination, JSONObject jsonMessage, long timeoutMillis){
        this(destination, jsonMessage, Duration.ofMillis(timeoutMillis));
    }

    public String getDestination() {
        return destination;
    }

    public Duration getTimeout() {
        return timeout;
    }

    public LocalDateTime getSendTime() {
        return sendTime;
    }

    public JSONObject getJsonMessage() {
        return jsonMessage;
    }
}
