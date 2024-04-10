package de.adesso.softwarepico.service.mirror;

import de.adesso.softwarepico.communication.hardware.errorHandling.DidNotRespondException;
import de.adesso.softwarepico.configuration.SensorStatus;
import de.adesso.softwarepico.service.SendingService;
import de.adesso.softwarepico.service.mirror.SoftwareRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
public class MirrorService {
    private final SendingService sendingService;
    private final SoftwareRepresentation softwareRepresentation;

    @Autowired
    public MirrorService(SendingService sendingService) {
        this.sendingService = sendingService;
        this.softwareRepresentation = new SoftwareRepresentation();
    }

    protected MirrorService(SendingService sendingService, SoftwareRepresentation softwareRepresentation){
        this.sendingService = sendingService;
        this.softwareRepresentation = softwareRepresentation;
    }

    public void initialize(int hardwarePicoId, String hardwarePicoIp){
        softwareRepresentation.initialize(hardwarePicoId, hardwarePicoIp);
        sendingService.bind(hardwarePicoIp);
    }

    public void rebind(String hardwarePicoIp){
        softwareRepresentation.setIp(hardwarePicoIp);
        sendingService.bind(hardwarePicoIp);
    }

    public void setSensorStatus(SensorStatus sensorStatus){
        softwareRepresentation.setStatus(sensorStatus);
        sendingService.setLed(softwareRepresentation.getIp(), softwareRepresentation.getLedStatus());
    }

    public void changeReservationStatus(boolean reserve){
        softwareRepresentation.setReserved(reserve);
        sendingService.setLed(softwareRepresentation.getIp(), softwareRepresentation.getLedStatus());
    }

    public SensorStatus getSensorStatus(){
        return softwareRepresentation.getStatus();
    }

    public void setConnectionStatus(String connectionStatus){
        sendingService.connectionInfo(softwareRepresentation.getId(), softwareRepresentation.getIp(), connectionStatus);
    }

    public void testConnection(){
        try {
            sendingService.ping(softwareRepresentation.getIp(), 1);
        }
        catch (DidNotRespondException e){
            setConnectionStatus("LOST");
        }
    }

    @Scheduled(fixedRate = 30000L)
    public void heartbeat(){
        if(softwareRepresentation.isInitialized()) {
            sendingService.ping(softwareRepresentation.getIp(), 0);
        }
    }


}
