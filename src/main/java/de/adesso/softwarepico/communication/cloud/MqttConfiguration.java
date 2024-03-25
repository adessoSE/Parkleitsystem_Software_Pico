package de.adesso.softwarepico.communication.cloud;

import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import de.adesso.softwarepico.configuration.MqttProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class MqttConfiguration {

    @Bean
    @Autowired
    public Mqtt5AsyncClient mqttClient(MqttProperties properties){
        Mqtt5AsyncClient client = MqttClient.builder()
                .identifier(UUID.randomUUID().toString())
                .serverHost(properties.getBrokerIPAddress())
                .serverPort(properties.getBrokerPort())
                .automaticReconnect()
                    .applyAutomaticReconnect()
                .useMqttVersion5()
                .buildAsync();
        client.connectWith()
                .cleanStart(true)
                .keepAlive(30)
                .simpleAuth()
                    .username(properties.getUserName())
                    .password(properties.getPassword().getBytes())
                    .applySimpleAuth()
                .send();
        return client;
    }

}
