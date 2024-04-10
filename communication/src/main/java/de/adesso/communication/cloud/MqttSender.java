package de.adesso.communication.cloud;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MqttSender implements CloudSender{
    private final Mqtt5AsyncClient mqttClient;

    @Autowired
    public MqttSender(Mqtt5AsyncClient mqttClient) {
        this.mqttClient = mqttClient;
    }

    public void send(String uri, JSONObject j){
        mqttClient.publishWith()
                .topic(uri)
                .payload(j.toString().getBytes())
                .qos(MqttQos.EXACTLY_ONCE)
                .send();
    }

}
