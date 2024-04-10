package de.adesso.communication.hardware;

import org.json.JSONObject;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@RestController
public class HttpReceiver implements HardwareReceiver{

    private final Map<String, List<Consumer<JSONObject>>> subscriptions;

    public HttpReceiver() {
        this.subscriptions = new HashMap<>();
    }

    public void subscribe(String uri, Consumer<JSONObject> jsonConsumer){
        if(subscriptions.containsKey(uri)){
            subscriptions.get(uri).add(jsonConsumer);
        }
        else {
            subscriptions.put(uri, List.of(jsonConsumer));
        }
    }

    @PostMapping("/software-pico")
    public ResponseEntity<String> register(@RequestBody String jsonString){
        JSONObject jsonObject = new JSONObject(jsonString);
        List<Consumer<JSONObject>> consumers = subscriptions.get("software-pico");
        for(Consumer<JSONObject> consumer : consumers){
            consumer.accept(jsonObject);
        }
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }

}
