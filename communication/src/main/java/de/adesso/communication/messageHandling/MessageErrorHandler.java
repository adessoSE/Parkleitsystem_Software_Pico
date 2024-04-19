package de.adesso.communication.messageHandling;

public interface MessageErrorHandler {

    boolean supports(Exception e);

    void handle(Exception e);

    default boolean isDefault(){
        return false;
    }


}
