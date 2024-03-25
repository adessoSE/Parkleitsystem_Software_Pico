package de.adesso.softwarepico;

import de.adesso.softwarepico.communication.Receiver;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class InboundHandlingService {

    private final Logger logger = LoggerFactory.getLogger(InboundHandlingService.class);
    private final MirrorService mirrorService;
    private final Receiver receiver;
    private final QueryService queryService;

    public InboundHandlingService(MirrorService mirrorService, @Qualifier("universalReceiver") Receiver receiver, QueryService queryService) {
        this.mirrorService = mirrorService;
        this.receiver = receiver;
        this.queryService = queryService;

        receiver.subscribe(SoftwarePicoApplication.uuid, this::handle);
        receiver.subscribe("software-pico",  this::handle);
    }

    public void handle(JSONObject j){
        if(j.has("origin") && j.get("origin").equals("cloud")){
            queryService.handleQuery(j);
            return;
        }
        if(j.has("messageType")){
            switch ((String) j.get("messageType")){
                case "bind" -> {
                    String hardwarePicoUri = (String) j.get("hardwarePicoUri");
                    String[] hardwarePicoInfo = hardwarePicoUri.split("/");
                    mirrorService.initialize(Integer.parseInt(hardwarePicoInfo[0]), hardwarePicoInfo[1], 1);
                    receiver.subscribe("software-pico/" + hardwarePicoInfo[0], this::handle);
                }
                case "rebind" -> {
                    String hardwarePicoUri = (String) j.get("hardwarePicoUri");
                    String[] hardwarePicoInfo = hardwarePicoUri.split("/");
                    mirrorService.initialize(Integer.parseInt(hardwarePicoInfo[0]), hardwarePicoInfo[1], 1);
                    logger.info("[Reestablished connection to Hardware-Pico]");
                }
                case "info" -> mirrorService.testConnection(1);
                case "sensor_info" -> {
                    int sensorId = j.getInt("sensorId");
                    String status = (String) j.get("status");
                    mirrorService.setSensorStatus(sensorId, status);
                }
                case "heartbeat" -> {
                    int importantFlag = j.getInt("important");
                    if(importantFlag == 1){
                        mirrorService.reportConnectionStatus("OK");
                    }
                }
                default -> logger.error("[Received message with unsupported message type: " + j.get("messageType") + "]");
            }
        }
        else{
            logger.error("[Received message without message type]");
        }
    }
}
