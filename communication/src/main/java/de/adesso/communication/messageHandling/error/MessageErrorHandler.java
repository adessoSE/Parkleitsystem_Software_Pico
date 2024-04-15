package de.adesso.communication.messageHandling.error;

public interface MessageErrorHandler {

    boolean supports(Exception e);

    void handle(Exception e);


}
