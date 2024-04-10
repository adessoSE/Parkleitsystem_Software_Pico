package de.adesso.softwarepico.messageHandling.handler;

import de.adesso.softwarepico.configuration.SensorStatus;
import de.adesso.softwarepico.messageHandling.MessageType;
import de.adesso.softwarepico.messageHandling.message.GetStatusMessage;
import de.adesso.softwarepico.messageHandling.message.Message;
import de.adesso.softwarepico.service.mirror.MirrorService;
import de.adesso.softwarepico.service.SendingService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class GetStatusHandlerTest {

    static MirrorService mirrorServiceMock;
    static SendingService sendingServiceMock;
    static GetStatusHandler getStatusHandlerToTest;

    @BeforeAll
    static void init(){
        mirrorServiceMock = mock(MirrorService.class);
        sendingServiceMock = mock(SendingService.class);
        getStatusHandlerToTest = new GetStatusHandler(mirrorServiceMock, sendingServiceMock);
    }

    @BeforeEach
    void resetMocks(){
        reset(mirrorServiceMock, sendingServiceMock);
    }

    @Test
    void supportsTrue(){

        Message m = mock(Message.class);

        when(m.getMessageType()).thenReturn(MessageType.GET_STATUS);

        assertTrue(getStatusHandlerToTest.supports(m));

    }

    @Test
    void supportsFalse(){

        for(MessageType mt : MessageType.values()){
            if(!mt.equals(MessageType.GET_STATUS)){
                Message m = mock(Message.class);
                when(m.getMessageType()).thenReturn(mt);

                assertFalse(getStatusHandlerToTest.supports(m));
            }

        }

    }

    @Test
    void handleSupported(){

        String sourceTopic = UUID.randomUUID().toString();
        String messageId = UUID.randomUUID().toString();
        SensorStatus status = SensorStatus.FREE;


        GetStatusMessage getStatusMessage = new GetStatusMessage(sourceTopic, messageId);

        when(mirrorServiceMock.getSensorStatus()).thenReturn(status);

        getStatusHandlerToTest.handle(getStatusMessage);

        verify(sendingServiceMock, times(1)).sendResponse(sourceTopic, messageId, status.name());

    }

    @Test
    void handleUnsupported(){

        for(MessageType mt : MessageType.values()){
            if(!mt.equals(MessageType.GET_STATUS)){
                Message m = mock(Message.class);
                when(m.getMessageType()).thenReturn(mt);

                getStatusHandlerToTest.handle(m);

                verify(sendingServiceMock, never()).sendResponse(anyString(), anyString(), anyString());
            }

        }

    }

}