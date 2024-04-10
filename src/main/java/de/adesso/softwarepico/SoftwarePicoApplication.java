package de.adesso.softwarepico;

import de.adesso.communication.cloud.MqttProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.UUID;

@SpringBootApplication
@EnableScheduling
@ComponentScan("de.adesso")
public class SoftwarePicoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoftwarePicoApplication.class, args);
    }

    private static final String uuid = UUID.randomUUID().toString();

    public static String getUuid(){
        return uuid;
    }

}
