package de.adesso.communication;

import org.json.JSONObject;

public interface Sender {

    void send(String uri, JSONObject j);

}
