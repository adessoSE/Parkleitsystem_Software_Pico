package de.adesso.softwarepico.service;

import de.adesso.communication.messaging.UniversalSender;
import de.adesso.softwarepico.SoftwarePicoApplication;
import de.adesso.softwarepico.configuration.LedStatus;
import jakarta.annotation.PostConstruct;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletableFuture;


@Service
public class SendingService {

    private final UniversalSender sender;
    private final CompletableFuture<Void> synchronizer;

    @Autowired
    public SendingService(UniversalSender sender, CompletableFuture<Void> synchronizer) {
        this.sender = sender;
        this.synchronizer = synchronizer;
    }

    public void bind(String ip){
        JSONObject bindMessage = new JSONObject().put("messageType", "bind").put("uri", SoftwarePicoApplication.getUuid());
        sender.send(ip, bindMessage);
    }

    public void setLed(String ip, LedStatus ledStatus){
        JSONObject setLedMessage = new JSONObject().put("messageType", "set_led").put("status", ledStatus.name());
        sender.send(ip, setLedMessage);
    }

    public void connectionInfo(int hardwarePicoId, String hardwarePicoIp, String connectionStatus){
        JSONObject connectionInfoMessage = new JSONObject()
                .put("messageType", "connection_info")
                .put("hardwarePicoUri", hardwarePicoId + "/" + hardwarePicoIp)
                .put("softwarePicoUri", SoftwarePicoApplication.getUuid())
                .put("connectionStatus", connectionStatus);
        sender.send("software-iot-gateway", connectionInfoMessage);
    }

    public void ping(String hardwarePicoIp, int important){
        JSONObject pingMessage = new JSONObject().put("messageType", "heartbeat").put("important", important);
        sender.sendExpectAnswer(hardwarePicoIp, pingMessage, Duration.of(5, ChronoUnit.SECONDS).toMillis());
    }

    @PostConstruct
    protected void init(){
        try {
            Thread.sleep(200);
        }catch (Exception ignore){}
        JSONObject registerMessage = new JSONObject().put("messageType", "register_sp").put("uri", SoftwarePicoApplication.getUuid());
        synchronizer.thenAccept(v -> sender.send("software-iot-gateway", registerMessage));
    }

    public void sendToDns(String uri){
        sender.publishToDns(uri);
    }


}
