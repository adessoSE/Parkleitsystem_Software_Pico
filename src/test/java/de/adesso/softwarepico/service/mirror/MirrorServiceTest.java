package de.adesso.softwarepico.service.mirror;

import de.adesso.communication.messageHandling.error.DidNotRespondException;
import de.adesso.softwarepico.configuration.LedStatus;
import de.adesso.softwarepico.configuration.SensorStatus;
import de.adesso.softwarepico.service.SendingService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MirrorServiceTest {

    static SendingService sendingServiceMock;
    static SoftwareRepresentation softwareRepresentationMock;
    static MirrorService mirrorServiceToTest;

    @BeforeAll
    static void init(){
        sendingServiceMock = mock(SendingService.class);
        softwareRepresentationMock = mock(SoftwareRepresentation.class);
        mirrorServiceToTest = spy(new MirrorService(sendingServiceMock, softwareRepresentationMock));
    }

    @BeforeEach
    void resetMocks(){
        reset(sendingServiceMock, softwareRepresentationMock, mirrorServiceToTest);
    }

    @Test
    void initialize() {

        int hardwarePicoId = 1;
        String hardwarePicoIp = "abc";

        mirrorServiceToTest.initialize(hardwarePicoId, hardwarePicoIp);

        verify(softwareRepresentationMock, times(1)).initialize(hardwarePicoId, hardwarePicoIp);
        verify(sendingServiceMock, times(1)).bind(hardwarePicoIp);

    }

    @Test
    void rebind() {

        String hardwarePicoIp = "abc";

        mirrorServiceToTest.rebind(hardwarePicoIp);

        verify(softwareRepresentationMock, times(1)).setIp(hardwarePicoIp);
        sendingServiceMock.bind(hardwarePicoIp);

    }

    @Test
    void setSensorStatus() {

        String hardwarePicoIp = "abc";

        for(SensorStatus s : SensorStatus.values()){

            when(softwareRepresentationMock.getIp()).thenReturn(hardwarePicoIp);

            mirrorServiceToTest.setSensorStatus(s);

            verify(softwareRepresentationMock, times(1)).setStatus(s);
            verify(sendingServiceMock, times(1)).setLed(eq(hardwarePicoIp), any());

            resetMocks();
        }

    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void changeReservationStatus(boolean reserve) {

        String hardwarePicoIp = "abc";
        LedStatus status = LedStatus.GREEN;

        when(softwareRepresentationMock.getIp()).thenReturn(hardwarePicoIp);
        when(softwareRepresentationMock.getLedStatus()).thenReturn(status);

        mirrorServiceToTest.changeReservationStatus(reserve);

        verify(softwareRepresentationMock, times(1)).setReserved(reserve);
        verify(sendingServiceMock, times(1)).setLed(hardwarePicoIp, status);

    }

    @Test
    void getSensorStatus() {

        SensorStatus status = SensorStatus.FREE;

        when(softwareRepresentationMock.getStatus()).thenReturn(status);

        assertEquals(status, mirrorServiceToTest.getSensorStatus());

    }

    @Test
    void setConnectionStatus() {

        int id = 1;
        String ip = "abc";

        String connectionStatus = "OK";

        when(softwareRepresentationMock.getId()).thenReturn(id);
        when(softwareRepresentationMock.getIp()).thenReturn(ip);

        mirrorServiceToTest.setConnectionStatus(connectionStatus);

        verify(sendingServiceMock, times(1)).connectionInfo(id, ip, connectionStatus);

    }

    @Test
    void testConnection() {

        String ip = "abc";

        when(softwareRepresentationMock.getIp()).thenReturn(ip);

        mirrorServiceToTest.testConnection();

        verify(sendingServiceMock, times(1)).ping(ip, 1);
    }

    @Test
    void testConnectionDidNotRespond(){

        String ip = "abc";

        when(softwareRepresentationMock.getIp()).thenReturn(ip);
        doThrow(new DidNotRespondException()).when(sendingServiceMock).ping(ip, 1);

        mirrorServiceToTest.testConnection();

        verify(mirrorServiceToTest, times(1)).setConnectionStatus("LOST");

    }

    @Test
    void heartbeatBase() {

        String ip = "abc";

        when(softwareRepresentationMock.getIp()).thenReturn(ip);
        when(softwareRepresentationMock.isInitialized()).thenReturn(true);

        mirrorServiceToTest.heartbeat();

        verify(sendingServiceMock, times(1)).ping(ip, 0);

    }

    @Test
    void heartbeatNotInitialized() {

        when(softwareRepresentationMock.isInitialized()).thenReturn(false);

        mirrorServiceToTest.heartbeat();

        verify(sendingServiceMock, never()).ping(anyString(), anyInt());

    }

}