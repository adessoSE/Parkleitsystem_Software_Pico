package de.adesso.communication.addressResolution.messageHandling;

import de.adesso.communication.messageHandling.Message;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DnsMessageHandlerTest {

    static Map<String, CompletableFuture<String>> pendingAnswersMock;
    static DnsMessageHandler dnsMessageHandlerToTest;

    @BeforeAll
    @SuppressWarnings("unchecked")
    static void init(){
        pendingAnswersMock = (Map<String, CompletableFuture<String>>) mock(Map.class);
        dnsMessageHandlerToTest = spy(new DnsMessageHandler(pendingAnswersMock));
    }

    @BeforeEach
    void resetMocks(){
        reset(pendingAnswersMock, dnsMessageHandlerToTest);
    }

    @Test
    @SuppressWarnings("unchecked")
    void handle() {

        String messageId = UUID.randomUUID().toString();
        String request = "service";
        String response = "topic";
        String domain = "domain";

        DnsMessage m = new DnsMessage(messageId, request, response, domain);
        CompletableFuture<String> completableFutureMock = (CompletableFuture<String>) mock(CompletableFuture.class);

        when(dnsMessageHandlerToTest.supports(m)).thenReturn(true);

        when(pendingAnswersMock.containsKey(messageId)).thenReturn(true);
        when(pendingAnswersMock.get(messageId)).thenReturn(completableFutureMock);

        dnsMessageHandlerToTest.handle(m);

        verify(completableFutureMock, times(1)).complete(domain + "/" + response);
        verify(pendingAnswersMock, times(1)).remove(messageId);

    }

    @Test
    void handleUnsupported(){

        Message m = mock(Message.class);
        doReturn(false).when(dnsMessageHandlerToTest).supports(m);

        verify(pendingAnswersMock, never());

    }

    @Test
    void supports() {

        Message m = mock(Message.class);

        when(m.getMessageType()).thenReturn(DnsMessageType.DNS_RESPONSE.name());

        assertTrue(dnsMessageHandlerToTest.supports(m));

    }
}