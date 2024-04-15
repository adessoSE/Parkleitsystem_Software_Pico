package de.adesso.communication.addressResolution;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DnsCacheTest {

    static DnsCache dnsCacheToTest;
    static Map<String, String> dnsCacheMapMock;
    static Map<String, LocalDateTime> timesToLiveMock;

    @BeforeAll
    @SuppressWarnings("unchecked")
    static void init(){
        dnsCacheMapMock = (Map<String, String>) mock(Map.class);
        timesToLiveMock = (Map<String, LocalDateTime>) mock(Map.class);
        dnsCacheToTest = spy(new DnsCache(dnsCacheMapMock, timesToLiveMock));
    }

    @BeforeEach
    void resetMocks(){
        reset(dnsCacheToTest, dnsCacheMapMock, timesToLiveMock);
    }


    @Test
    void hasEntryFor() {

        String service = "service";

        when(dnsCacheMapMock.containsKey(service)).thenReturn(true);
        when(timesToLiveMock.get(service)).thenReturn(LocalDateTime.now().minusSeconds(30));

        assertTrue(dnsCacheToTest.hasEntryFor(service));

    }

    @Test
    void hasNoEntryFor(){

        String service = "service";

        when(dnsCacheMapMock.containsKey(service)).thenReturn(false);

        assertFalse(dnsCacheToTest.hasEntryFor(service));

    }

    @Test
    void hasNoValidEntryFor(){

        String service = "service";

        when(dnsCacheMapMock.containsKey(service)).thenReturn(true);
        when(timesToLiveMock.get(service)).thenReturn(LocalDateTime.now().minusSeconds(90));

        assertFalse(dnsCacheToTest.hasEntryFor(service));

    }

    @Test
    void getAddressHasEntry() {

        String service = "service";
        String domainAndTopic = "domain/topic";

        doReturn(true).when(dnsCacheToTest).hasEntryFor(service);
        when(dnsCacheMapMock.get(service)).thenReturn(domainAndTopic);

        assertEquals(domainAndTopic, dnsCacheToTest.getAddress(service));

    }

    @Test
    void getAddressHasNotEntry(){

        String service = "service";

        doReturn(false).when(dnsCacheToTest).hasEntryFor(service);

        assertNull(dnsCacheToTest.getAddress(service));

    }

    @Test
    void addEntry() {

        String service = "service";
        String domainAndTopic = "domain/topic";
        LocalDateTime now = LocalDateTime.now();

        MockedStatic<LocalDateTime> localDateTimeMock = mockStatic(LocalDateTime.class);

        localDateTimeMock.when(LocalDateTime::now).thenReturn(now);

        dnsCacheToTest.addEntry(service, domainAndTopic);

        verify(dnsCacheMapMock, times(1)).put(service, domainAndTopic);
        verify(timesToLiveMock, times(1)).put(service, now);

        localDateTimeMock.close();

    }

    @Test
    void checkTimesToLive() {

        String service = "service";
        LocalDateTime now = LocalDateTime.now();

        MockedStatic<LocalDateTime> localDateTimeMock = mockStatic(LocalDateTime.class);

        localDateTimeMock.when(LocalDateTime::now).thenReturn(now);
        when(dnsCacheMapMock.keySet()).thenReturn(Set.of(service));
        when(timesToLiveMock.get(service)).thenReturn(now.minusMinutes(2));

        dnsCacheToTest.checkTimesToLive();

        verify(dnsCacheMapMock, times(1)).remove(service);
        verify(timesToLiveMock, times(1)).remove(service);

        localDateTimeMock.close();
    }
}