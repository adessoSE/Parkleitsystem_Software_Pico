package de.adesso.softwarepico.service;

import de.adesso.softwarepico.SoftwarePicoApplication;
import de.adesso.communication.cloud.CloudSender;
import de.adesso.communication.hardware.HardwareSender;
import de.adesso.softwarepico.configuration.LedStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SendingServiceTest {
    static CloudSender cloudSenderMock;
    static HardwareSender hardwareSenderMock;
    static SendingService sendingServiceToTest;

    @BeforeAll
    static void init(){
        cloudSenderMock = mock(CloudSender.class);
        hardwareSenderMock = mock(HardwareSender.class);

        sendingServiceToTest = spy(new SendingService(cloudSenderMock, hardwareSenderMock));
    }

    @BeforeEach
    void resetMocks(){
        reset(cloudSenderMock, hardwareSenderMock);
    }

    @Test
    void bind() {

        String ip = "abc";

        ArgumentCaptor<JSONObject> captor = ArgumentCaptor.captor();

        sendingServiceToTest.bind(ip);

        verify(hardwareSenderMock, times(1)).send(eq(ip), captor.capture());

        JSONObject value = captor.getValue();

        assertEquals("bind", value.getString("messageType"));

    }

    @Test
    void setLed() {

        String ip = "abc";
        LedStatus ledStatus = LedStatus.GREEN;

        ArgumentCaptor<JSONObject> captor = ArgumentCaptor.captor();

        sendingServiceToTest.setLed(ip, ledStatus);

        verify(hardwareSenderMock, times(1)).send(eq(ip), captor.capture());

        JSONObject value = captor.getValue();

        assertEquals("setLed", value.getString("messageType"));
        assertEquals(ledStatus.name(), value.getString("status"));

    }

    @Test
    void connectionInfo() {

        int id = 1;
        String ip = "abc";
        String connectionStatus = "OK";
        String uuid = UUID.randomUUID().toString();

        ArgumentCaptor<JSONObject> captor = ArgumentCaptor.captor();

        MockedStatic<SoftwarePicoApplication> softwarePicoApplicationMock = mockStatic(SoftwarePicoApplication.class);
        softwarePicoApplicationMock.when(SoftwarePicoApplication::getUuid).thenReturn(uuid);

        sendingServiceToTest.connectionInfo(id, ip, connectionStatus);

        verify(cloudSenderMock, times(1)).send(eq("software-iot-gateway"), captor.capture());

        JSONObject value = captor.getValue();

        assertEquals("connection_info", value.getString("messageType"));
        assertEquals(id + "/" + ip, value.getString("hardwarePicoUri"));
        assertEquals(uuid, value.getString("softwarePicoUri"));
        assertEquals(connectionStatus, value.getString("connectionStatus"));

        softwarePicoApplicationMock.close();

    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void ping(int important) {

        String hardwarePicoIp = "abc";

        ArgumentCaptor<JSONObject> captor = ArgumentCaptor.captor();

        sendingServiceToTest.ping(hardwarePicoIp, important);

        verify(hardwareSenderMock, times(1)).send(eq(hardwarePicoIp), captor.capture());

        JSONObject value = captor.getValue();

        assertEquals("heartbeat", value.getString("messageType"));
        assertEquals(important, value.getInt("important"));

    }

    @Test
    void sendResponse() {

        String uri = "any-valid-mqtt-topic";
        String messageId = UUID.randomUUID().toString();
        String status = "FREE";

        ArgumentCaptor<JSONObject> captor = ArgumentCaptor.captor();

        sendingServiceToTest.sendResponse(uri, messageId, status);

        verify(cloudSenderMock, times(1)).send(eq(uri), captor.capture());

        JSONObject value = captor.getValue();

        assertEquals("statusResponse", value.getString("messageType"));
        assertEquals(messageId, value.getString("messageId"));
        assertEquals(status, value.getString("status"));

    }

    @Test
    void registerToIotGateway(){

        String uuid = UUID.randomUUID().toString();

        ArgumentCaptor<JSONObject> captor = ArgumentCaptor.captor();

        MockedStatic<SoftwarePicoApplication> softwarePicoApplicationMock = mockStatic(SoftwarePicoApplication.class);
        softwarePicoApplicationMock.when(SoftwarePicoApplication::getUuid).thenReturn(uuid);

        sendingServiceToTest.init();

        verify(cloudSenderMock, times(1)).send(eq("software-iot-gateway"), captor.capture());

        JSONObject value = captor.getValue();

        assertEquals("register_sp", value.getString("messageType"));
        assertEquals(uuid, value.getString("uri"));

        softwarePicoApplicationMock.close();

    }
}