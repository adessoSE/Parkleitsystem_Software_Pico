package de.adesso.softwarepico.communication;

import org.json.JSONObject;

public interface Sender {

    void send(String uri, JSONObject j);

}
