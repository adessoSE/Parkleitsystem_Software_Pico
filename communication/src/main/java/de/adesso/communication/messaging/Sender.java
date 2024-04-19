package de.adesso.communication.messaging;

import org.json.JSONObject;

public interface Sender {

    void send(String uri, JSONObject j);

    boolean supports(String domain);

}
