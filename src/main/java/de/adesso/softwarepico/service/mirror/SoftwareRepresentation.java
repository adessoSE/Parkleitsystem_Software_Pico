package de.adesso.softwarepico.service.mirror;

import de.adesso.softwarepico.configuration.LedStatus;
import de.adesso.softwarepico.configuration.SensorStatus;

public class SoftwareRepresentation {
    private int id;
    private String ip;
    private SensorStatus status;
    private LedStatus ledStatus;
    private boolean isReserved;

    public SoftwareRepresentation(){
        this.id = -1;
        this.ip = "....";
        this.status = SensorStatus.UNKNOWN;
        this.ledStatus = LedStatus.UNKNOWN;
        this.isReserved = false;
    }

    public boolean isInitialized(){
        return id != -1 && !ip.equals("....");
    }

    public void initialize(int id, String ip){
        if(!isInitialized()) {
            this.id = id;
            this.ip = ip;
            return;
        }
        throw new IllegalArgumentException("Software-representation is already initialized");
    }

    public int getId() {
        return id;
    }

    public String getIp() {
        return ip;
    }

    public SensorStatus getStatus() {
        return status;
    }

    public LedStatus getLedStatus() {
        return ledStatus;
    }

    public boolean isReserved() {
        return isReserved;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setStatus(SensorStatus status) {
        this.status = status;

        this.ledStatus = computeLedStatus(status, isReserved());
    }

    protected LedStatus computeLedStatus(SensorStatus status, boolean isReserved){
        if(status.equals(SensorStatus.BLOCKED)){
            return LedStatus.RED;
        }
        else if(isReserved){
            return LedStatus.YELLOW;
        }
        else if(status.equals(SensorStatus.FREE)){
             return LedStatus.GREEN;
        }
        else{
            return LedStatus.NONE;
        }
    }


    public void setReserved(boolean reserved) {
        isReserved = reserved;
        setStatus(getStatus());
    }
}
