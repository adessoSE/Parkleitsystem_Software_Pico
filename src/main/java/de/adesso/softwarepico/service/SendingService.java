package de.adesso.softwarepico.service;

import de.adesso.softwarepico.SoftwarePicoApplication;
import de.adesso.softwarepico.communication.cloud.CloudSender;
import de.adesso.softwarepico.communication.hardware.HardwareSender;
import de.adesso.softwarepico.configuration.LedStatus;
import jakarta.annotation.PostConstruct;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SendingService {
    private final CloudSender cloudSender;
    private final HardwareSender hardwareSender;

    @Autowired
    public SendingService(CloudSender cloudSender, HardwareSender hardwareSender) {
        this.cloudSender = cloudSender;
        this.hardwareSender = hardwareSender;
    }

    public void bind(String ip){
        JSONObject bindMessage = new JSONObject().put("messageType", "bind");
        hardwareSender.send(ip, bindMessage);
    }

    public void setLed(String ip, LedStatus ledStatus){
        JSONObject setLedMessage = new JSONObject().put("messageType", "setLed").put("status", ledStatus.name());
        hardwareSender.send(ip, setLedMessage);
    }

    public void connectionInfo(int hardwarePicoId, String hardwarePicoIp, String connectionStatus){
        JSONObject connectionInfoMessage = new JSONObject()
                .put("messageType", "connection_info")
                .put("hardwarePicoUri", hardwarePicoId + "/" + hardwarePicoIp)
                .put("softwarePicoUri", SoftwarePicoApplication.getUuid())
                .put("connectionStatus", connectionStatus);
        cloudSender.send("software-iot-gateway", connectionInfoMessage);
    }

    public void ping(String hardwarePicoIp, int important){
        JSONObject pingMessage = new JSONObject().put("messageType", "heartbeat").put("important", important);
        hardwareSender.send(hardwarePicoIp, pingMessage);
    }

    public void sendResponse(String uri, String messageId, String status){
        JSONObject response = new JSONObject().put("messageType", "statusResponse").put("messageId", messageId).put("status", status);
        cloudSender.send(uri, response);
    }

    @PostConstruct
    protected void init(){
        try {
            Thread.sleep(100);
        }catch (Exception ignore){}
        JSONObject registerMessage = new JSONObject().put("messageType", "register_sp").put("uri", SoftwarePicoApplication.getUuid());
        cloudSender.send("software-iot-gateway", registerMessage);
    }


}
