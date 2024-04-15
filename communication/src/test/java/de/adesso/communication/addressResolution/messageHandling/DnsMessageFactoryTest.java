package de.adesso.communication.addressResolution.messageHandling;

import de.adesso.communication.messageHandling.Message;
import de.adesso.communication.messageHandling.error.JsonMessageNotSupportedException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DnsMessageFactoryTest {

    static DnsMessageFactory dnsMessageFactoryToTest;

    @BeforeAll
    static void init(){
        dnsMessageFactoryToTest = spy(new DnsMessageFactory());
    }

    @BeforeEach
    void resetMocks(){
        reset(dnsMessageFactoryToTest);
    }

    @Test
    void fromJsonDnsResponse() {

        String messageType = "dns_response";
        String messageId = UUID.randomUUID().toString();
        String request = "serviceToRequest";
        String response = "topicForService";
        String domain = "domainForService";

        JSONObject jsonMessageMock = mock(JSONObject.class);

        when(dnsMessageFactoryToTest.supports(jsonMessageMock)).thenReturn(true);
        when(jsonMessageMock.getString("messageType")).thenReturn(messageType);
        when(jsonMessageMock.getString("messageId")).thenReturn(messageId);
        when(jsonMessageMock.getString("request")).thenReturn(request);
        when(jsonMessageMock.getString("response")).thenReturn(response);
        when(jsonMessageMock.getString("domain")).thenReturn(domain);

        Message m = dnsMessageFactoryToTest.fromJson(jsonMessageMock);

        assertInstanceOf(DnsMessage.class, m);

        DnsMessage dnsMessage = (DnsMessage) m;
        assertEquals(messageId, dnsMessage.messageId());
        assertEquals(request, dnsMessage.request());
        assertEquals(response, dnsMessage.response());
        assertEquals(domain, dnsMessage.domain());

    }

    @Test
    void fromJsonDoesNotSupport(){

        JSONObject jsonMessageMock = mock(JSONObject.class);

        when(dnsMessageFactoryToTest.supports(jsonMessageMock)).thenReturn(false);

        assertThrows(JsonMessageNotSupportedException.class, () -> dnsMessageFactoryToTest.fromJson(jsonMessageMock));

    }

    @Test
    void supports() {

        for(DnsMessageType dnsMessageType : DnsMessageType.values()){

            JSONObject jsonMessageMock = mock(JSONObject.class);
            when(jsonMessageMock.has("messageType")).thenReturn(true);
            when(jsonMessageMock.getString("messageType")).thenReturn(dnsMessageType.name());

            assertTrue(dnsMessageFactoryToTest.supports(jsonMessageMock));

        }

    }
}