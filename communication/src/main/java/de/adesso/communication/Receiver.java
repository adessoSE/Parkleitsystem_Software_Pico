package de.adesso.communication;

import org.json.JSONObject;

import java.util.function.Consumer;

public interface Receiver {

    void subscribe(String uri, Consumer<JSONObject> jsonConsumer);

}
