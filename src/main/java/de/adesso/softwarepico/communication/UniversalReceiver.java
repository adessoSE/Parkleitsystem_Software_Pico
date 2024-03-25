package de.adesso.softwarepico.communication;

import de.adesso.softwarepico.communication.cloud.CloudReceiver;
import de.adesso.softwarepico.communication.hardware.HardwareReceiver;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class UniversalReceiver implements Receiver{

    private final CloudReceiver cloudReceiver;
    private final HardwareReceiver hardwareReceiver;

    public UniversalReceiver(CloudReceiver cloudReceiver, HardwareReceiver hardwareReceiver) {
        this.cloudReceiver = cloudReceiver;
        this.hardwareReceiver = hardwareReceiver;
    }

    @Override
    public void subscribe(String uri, Consumer<JSONObject> jsonConsumer) {
        cloudReceiver.subscribe(uri, jsonConsumer);
        hardwareReceiver.subscribe(uri, jsonConsumer);
    }
}
