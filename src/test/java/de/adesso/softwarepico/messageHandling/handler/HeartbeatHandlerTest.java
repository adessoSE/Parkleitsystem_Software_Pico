package de.adesso.softwarepico.messageHandling.handler;

import de.adesso.softwarepico.messageHandling.MessageType;
import de.adesso.softwarepico.messageHandling.message.HeartBeatMessage;
import de.adesso.softwarepico.messageHandling.message.Message;
import de.adesso.softwarepico.service.mirror.MirrorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class HeartbeatHandlerTest {

    static MirrorService mirrorServiceMock;
    static HeartbeatHandler heartbeatHandlerToTest;

    @BeforeAll
    static void init(){
        mirrorServiceMock = mock(MirrorService.class);
        heartbeatHandlerToTest = new HeartbeatHandler(mirrorServiceMock);
    }

    @BeforeEach
    void resetMocks(){
        reset(mirrorServiceMock);
    }

    @Test
    void supportsTrue(){

        Message m = mock(Message.class);

        when(m.getMessageType()).thenReturn(MessageType.HEARTBEAT);

        assertTrue(heartbeatHandlerToTest.supports(m));

    }

    @Test
    void supportsFalse(){

        for(MessageType mt : MessageType.values()){
            if(!mt.equals(MessageType.HEARTBEAT)){
                Message m = mock(Message.class);
                when(m.getMessageType()).thenReturn(mt);

                assertFalse(heartbeatHandlerToTest.supports(m));
            }

        }

    }

    @Test
    void handleSupportedImportant(){

        int importantFlag = 1;

        HeartBeatMessage heartBeatMessage = new HeartBeatMessage(importantFlag);

        heartbeatHandlerToTest.handle(heartBeatMessage);

        verify(mirrorServiceMock, times(1)).setConnectionStatus("OK");

    }

    @Test
    void handleSupportedUnimportant(){

        int importantFlag = 0;

        HeartBeatMessage heartBeatMessage = new HeartBeatMessage(importantFlag);

        heartbeatHandlerToTest.handle(heartBeatMessage);

        verify(mirrorServiceMock, never()).setConnectionStatus(anyString());

    }

    @Test
    void handleUnsupported(){

        for(MessageType mt : MessageType.values()){
            if(!mt.equals(MessageType.HEARTBEAT)){
                Message m = mock(Message.class);
                when(m.getMessageType()).thenReturn(mt);

                heartbeatHandlerToTest.handle(m);

                verify(mirrorServiceMock, never()).setConnectionStatus(anyString());
            }

        }

    }

}