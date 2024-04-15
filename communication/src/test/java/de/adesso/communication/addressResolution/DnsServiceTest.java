package de.adesso.communication.addressResolution;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DnsServiceTest {

    static Map<String, CompletableFuture<String>> pendingAnswersMock;
    static DnsCache dnsCacheMock;
    static DnsSendingService dnsSendingServiceMock;
    static DnsService dnsServiceToTest;

    @BeforeAll
    @SuppressWarnings("unchecked")
    static void init(){
        pendingAnswersMock = (Map<String, CompletableFuture<String>>) mock(Map.class);
        dnsCacheMock = mock(DnsCache.class);
        dnsSendingServiceMock = mock(DnsSendingService.class);
        dnsServiceToTest = spy(new DnsService(pendingAnswersMock, dnsCacheMock, dnsSendingServiceMock));
    }

    @BeforeEach
    void resetMocks(){
        reset(pendingAnswersMock, dnsCacheMock, dnsServiceToTest, dnsSendingServiceMock);
    }

    @Test
    void getTopicForServiceFromCache() throws ExecutionException, InterruptedException {

        String service = "service";
        String domainAndTopic = "domain/topic";

        when(dnsCacheMock.hasEntryFor(service)).thenReturn(true);
        when(dnsCacheMock.getAddress(service)).thenReturn(domainAndTopic);

        assertEquals(domainAndTopic, dnsServiceToTest.getTopicForService(service).get());

    }

    @Test
    void getTopicForServiceAsync(){

        String service = "service";
        String domainAndTopic = "domain/topic";

        when(dnsCacheMock.hasEntryFor(service)).thenReturn(false);

        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        doReturn(completableFuture).when(dnsServiceToTest).getTopicAsync(service);

        completableFuture.complete(domainAndTopic);

        assertEquals(completableFuture, dnsServiceToTest.getTopicForService(service));

        verify(dnsCacheMock, times(1)).addEntry(service, domainAndTopic);

    }

    @Test
    void getTopicAsync(){

        String service = "service";
        UUID messageId = UUID.randomUUID();

        MockedStatic<UUID> uuidMock = mockStatic(UUID.class);

        uuidMock.when(UUID::randomUUID).thenReturn(messageId);

        CompletableFuture<String> completableFuture = dnsServiceToTest.getTopicAsync(service);

        verify(pendingAnswersMock, times(1)).put(messageId.toString(), completableFuture);
        verify(dnsSendingServiceMock, times(1)).dnsRequest(messageId.toString(), service);

        uuidMock.close();

    }

}