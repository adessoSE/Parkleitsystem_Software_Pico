package de.adesso.softwarepico;

import de.adesso.softwarepico.communication.cloud.CloudSender;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QueryService {

    private final MirrorService mirrorService;
    private final CloudSender cloudSender;
    private final Logger logger = LoggerFactory.getLogger(QueryService.class);

    @Autowired
    public QueryService(MirrorService mirrorService, CloudSender cloudSender) {
        this.mirrorService = mirrorService;
        this.cloudSender = cloudSender;
    }

    public void handleQuery(JSONObject j){
        if(j.has("messageType")){
            switch ((String) j.get("messageType")){
                case "status" -> {
                    int sensorId = j.getInt("sensorId");
                    String sourceTopic = (String) j.get("source");
                    SensorStatus status = mirrorService.getSensorStatus(sensorId);
                    JSONObject response = new JSONObject().put("messageType", "status").put("source", mirrorService.getSemanticTopic()).put("status", status);
                    cloudSender.send(sourceTopic, response);
                }
                case "reserve" -> {
                    int cancelFlag = j.getInt("cancel");
                    int sensorId = j.getInt("sensorId");
                    if(cancelFlag == 1){
                        mirrorService.cancelReservation(sensorId);
                        logger.info("[Canceled reservation sensor ID=" + sensorId + "]");
                    }
                    else {
                        mirrorService.reserve(sensorId);
                        logger.info("[Reserved sensor ID=" + sensorId + "]");
                    }
                }
                default -> logger.error("[Received message with unsupported message type: " + j.get("messageType") + "]");
            }
        }
        else {
            logger.error("[Received message without message type]");
        }
    }
}
