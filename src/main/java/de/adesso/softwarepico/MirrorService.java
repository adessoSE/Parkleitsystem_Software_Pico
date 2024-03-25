package de.adesso.softwarepico;

import de.adesso.softwarepico.communication.cloud.CloudSender;
import de.adesso.softwarepico.communication.hardware.HardwareSender;
import jakarta.annotation.PostConstruct;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import static de.adesso.softwarepico.SoftwarePicoApplication.uuid;

import java.util.HashMap;
import java.util.Map;

@Service
public class MirrorService {

    private final HardwareSender hardwareSender;
    private final CloudSender cloudSender;
    private int hardwarePicoId;
    private String hardwarePicoIp;
    private int managedSensors;
    private final Map<Integer, SensorStatus> sensorStatusMap;
    private final Map<Integer, LedStatus> ledStatusMap;
    private boolean initialized = false;
    private final Logger logger = LoggerFactory.getLogger(MirrorService.class);
    private boolean[] isReserved;

    @Autowired
    public MirrorService(HardwareSender hardwareSender, CloudSender cloudSender) {
        this.hardwareSender = hardwareSender;
        this.cloudSender = cloudSender;
        sensorStatusMap = new HashMap<>();
        ledStatusMap = new HashMap<>();
    }
    public void initialize(int hardwarePicoId, String hardwarePicoIp, int managedSensors){
        this.hardwarePicoId = hardwarePicoId;
        this.hardwarePicoIp = hardwarePicoIp;
        this.managedSensors = managedSensors;
        for(int i = 0; i < managedSensors; i++){
            sensorStatusMap.put(i, SensorStatus.FREE);
            ledStatusMap.put(i, LedStatus.UNKNOWN);
        }
        JSONObject registerMessage = new JSONObject().put("messageType", "bind");
        hardwareSender.send(hardwarePicoIp, registerMessage);
        isReserved = new boolean[managedSensors];
        initialized = true;
    }
    public void setSensorStatus(int sensorId, String status){
        if(initialized) {
            sensorStatusMap.put(sensorId, SensorStatus.valueOf(status));
            ledStatusMap.put(sensorId, LedStatus.valueOf(convertSensorStatusToLedStatus(status, sensorId)));
            JSONObject setLedMessage = new JSONObject().put("messageType", "setLed").put("sensorId", sensorId).put("status", ledStatusMap.get(sensorId));
            hardwareSender.send(hardwarePicoIp, setLedMessage);
        }
    }

    private String convertSensorStatusToLedStatus(String sensorStatus, int sensorId){
        if(sensorStatus.equals("FREE")){
            if(isReserved[sensorId]){
                return "YELLOW";
            }
            return "GREEN";
        }
        return switch (sensorStatus){
            case "BLOCKED" -> "RED";
            case "DEFECT" -> "NONE";
            default -> "UNKNOWN";
        };
    }

    @Scheduled(fixedRate = 30000L)
    private void testConnection(){
        testConnection(0);
    }

    public void testConnection(int i){
        try {
            if(initialized) {
                JSONObject heartbeatMessage = new JSONObject().put("messageType", "heartbeat").put("important", i);
                hardwareSender.send(hardwarePicoIp, heartbeatMessage);
            }
        }
        catch (Exception e){
            logger.warn("[Lost connection to Hardware-Pico]");
            if(i == 1){
                reportConnectionStatus("LOST");
            }
            else {
                logger.info("[Trying again in 30s]");
            }
        }

    }

    @PostConstruct
    private void init(){ 
        try {
            Thread.sleep(100);
        }catch (Exception ignore){}
        JSONObject registerMessage = new JSONObject().put("messageType", "register_sp").put("uri", uuid);
        cloudSender.send("software-iot-gateway", registerMessage);
    }

    public void reportConnectionStatus(String statusCode){
        JSONObject connectionInfoMessage = new JSONObject().put("messageType", "connection_info").put("hardwarePicoUri", hardwarePicoId + "/" + hardwarePicoIp).put("softwarePicoUri", uuid).put("connectionStatus", statusCode);
        cloudSender.send("software-iot-gateway", connectionInfoMessage);
    }

    public SensorStatus getSensorStatus(int sensorId){
        return sensorStatusMap.get(sensorId);
    }

    public String getSemanticTopic(){
        return "software-pico/" + hardwarePicoId;
    }

    public void reserve(int sensorId) {
        isReserved[sensorId] = true;
        setSensorStatus(sensorId, sensorStatusMap.get(sensorId).name());
    }
    public void cancelReservation(int sensorId){
        isReserved[sensorId] = false;
        setSensorStatus(sensorId, sensorStatusMap.get(sensorId).name());
    }
}
