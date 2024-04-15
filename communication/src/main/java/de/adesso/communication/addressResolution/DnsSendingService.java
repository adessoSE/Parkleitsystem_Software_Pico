package de.adesso.communication.addressResolution;

import de.adesso.communication.messaging.Sender;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class DnsSendingService {

    private final Sender sender;
    private final String uuid;

    @Autowired
    public DnsSendingService(@Qualifier("mqttSender") Sender sender, @Qualifier("uuid") String uuid) {
        this.sender = sender;
        this.uuid = uuid;
    }

    public void dnsRequest(String messageId, String request){
        JSONObject dnsRequest = new JSONObject()
                .put("messageType", "dns_request")
                .put("source", uuid)
                .put("messageId", messageId)
                .put("request", request);
        sender.send("cloud-dns", dnsRequest);
    }
}
