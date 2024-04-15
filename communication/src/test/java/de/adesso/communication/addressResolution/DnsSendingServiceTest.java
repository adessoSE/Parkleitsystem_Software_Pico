package de.adesso.communication.addressResolution;

import de.adesso.communication.messaging.Sender;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DnsSendingServiceTest {

    static Sender senderMock;
    static String uuid;
    static DnsSendingService dnsSendingServiceToTest;

    @BeforeAll
    static void init(){
        senderMock = mock(Sender.class);
        uuid = UUID.randomUUID().toString();
        dnsSendingServiceToTest = new DnsSendingService(senderMock, uuid);
    }


    @Test
    void dnsRequest() {

        String messageId = UUID.randomUUID().toString();
        String request = "service";

        ArgumentCaptor<JSONObject> captor = ArgumentCaptor.captor();

        dnsSendingServiceToTest.dnsRequest(messageId, request);

        verify(senderMock, times(1)).send(eq("cloud-dns"), captor.capture());

        JSONObject value = captor.getValue();

        assertEquals("dns_request", value.getString("messageType"));
        assertEquals(uuid, value.getString("source"));
        assertEquals(messageId, value.getString("messageId"));
        assertEquals(request, value.getString("request"));

    }
}