package de.adesso.softwarepico.messageHandling;

import de.adesso.softwarepico.messageHandling.message.*;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MessageFactoryTest {

    @Test
    void fromJsonBind(){

        int id = 1;
        String ip = "abc";

        String uri = id + "/" + ip;

        JSONObject jsonObjectMock = mock(JSONObject.class);
        when(jsonObjectMock.getString("messageType")).thenReturn("bind");
        when(jsonObjectMock.getString("hardwarePicoUri")).thenReturn(uri);

        Message m = MessageFactory.fromJson(jsonObjectMock);

        assertInstanceOf(BindMessage.class, m);
        assertEquals(id, ((BindMessage) m).hardwarePicoId());
        assertEquals(ip, ((BindMessage) m).hardwarePicoIp());

    }

    @Test
    void fromJsonRebind(){

        int id = 1;
        String ip = "abc";

        String uri = id + "/" + ip;

        JSONObject jsonObjectMock = mock(JSONObject.class);
        when(jsonObjectMock.getString("messageType")).thenReturn("rebind");
        when(jsonObjectMock.getString("hardwarePicoUri")).thenReturn(uri);

        Message m = MessageFactory.fromJson(jsonObjectMock);

        assertInstanceOf(RebindMessage.class, m);
        assertEquals(ip, ((RebindMessage) m).hardwarePicoIp());

    }

    @Test
    void fromJsonInfo(){

        JSONObject jsonObjectMock = mock(JSONObject.class);
        when(jsonObjectMock.getString("messageType")).thenReturn("info");

        Message m = MessageFactory.fromJson(jsonObjectMock);

        assertInstanceOf(InfoMessage.class, m);

    }

    @Test
    void fromJsonSensorInfo(){

        String status = "free";

        JSONObject jsonObjectMock = mock(JSONObject.class);
        when(jsonObjectMock.getString("messageType")).thenReturn("sensor_info");
        when(jsonObjectMock.getString("status")).thenReturn(status);

        Message m = MessageFactory.fromJson(jsonObjectMock);

        assertInstanceOf(SensorInfoMessage.class, m);
        assertEquals(status, ((SensorInfoMessage) m).status());

    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void fromJsonHeartbeat(int important){

        JSONObject jsonObjectMock = mock(JSONObject.class);
        when(jsonObjectMock.getString("messageType")).thenReturn("heartbeat");
        when(jsonObjectMock.getInt("important")).thenReturn(important);

        Message m = MessageFactory.fromJson(jsonObjectMock);

        assertInstanceOf(HeartBeatMessage.class, m);
        assertEquals(important, ((HeartBeatMessage) m).importantFlag());

    }

    @Test
    void fromJsonGetStatus(){

        String sourceTopic = UUID.randomUUID().toString();
        String messageId = UUID.randomUUID().toString();

        JSONObject jsonObjectMock = mock(JSONObject.class);
        when(jsonObjectMock.getString("messageType")).thenReturn("get_status");
        when(jsonObjectMock.getString("source")).thenReturn(sourceTopic);
        when(jsonObjectMock.getString("messageId")).thenReturn(messageId);

        Message m = MessageFactory.fromJson(jsonObjectMock);

        assertInstanceOf(GetStatusMessage.class, m);
        assertEquals(sourceTopic, ((GetStatusMessage) m).sourceTopic());
        assertEquals(messageId, ((GetStatusMessage) m).messageId());

    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void fromJsonReservation(boolean reserve){

        JSONObject jsonObjectMock = mock(JSONObject.class);
        when(jsonObjectMock.getString("messageType")).thenReturn("reserve");
        when(jsonObjectMock.getBoolean("reserve")).thenReturn(reserve);

        Message m = MessageFactory.fromJson(jsonObjectMock);

        assertInstanceOf(ReservationMessage.class, m);
        assertEquals(reserve, ((ReservationMessage) m).reserve());

    }

}