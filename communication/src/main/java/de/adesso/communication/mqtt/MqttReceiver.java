package de.adesso.communication.mqtt;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import de.adesso.communication.messaging.Receiver;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class MqttReceiver implements Receiver {

    private final Mqtt5AsyncClient mqttClient;

    @Autowired
    public MqttReceiver(Mqtt5AsyncClient mqttClient) {
        this.mqttClient = mqttClient;
    }

    public void subscribe(String uri, Consumer<JSONObject> jsonConsumer){
        mqttClient.subscribeWith()
                .topicFilter(uri)
                .qos(MqttQos.EXACTLY_ONCE)
                .callback(mqtt5Publish -> mqtt5Publish.getPayload().ifPresent(byteBuffer -> {
                    byte[] bytes = new byte[byteBuffer.remaining()];
                    byteBuffer.get(bytes);
                    JSONObject payload = new JSONObject(new String(bytes));
                    jsonConsumer.accept(payload);
            })).send();
    }
}
