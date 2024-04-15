package de.adesso.communication.messageHandling;

public interface MessageHandler {

    <T extends Message> void handle(T message);

    <T extends Message> boolean supports(T message);

    default boolean isDefault(){
        return false;
    }

}
