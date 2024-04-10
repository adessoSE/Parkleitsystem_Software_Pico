package de.adesso.softwarepico.messageHandling.handler;

import de.adesso.softwarepico.messageHandling.MessageType;
import de.adesso.softwarepico.messageHandling.message.InfoMessage;
import de.adesso.softwarepico.messageHandling.message.Message;
import de.adesso.softwarepico.service.mirror.MirrorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InfoHandlerTest {

    static MirrorService mirrorServiceMock;
    static InfoHandler infoHandlerToTest;

    @BeforeAll
    static void init(){
        mirrorServiceMock = mock(MirrorService.class);
        infoHandlerToTest = new InfoHandler(mirrorServiceMock);
    }

    @BeforeEach
    void resetMocks(){
        reset(mirrorServiceMock);
    }

    @Test
    void supportsTrue(){

        Message m = mock(Message.class);

        when(m.getMessageType()).thenReturn(MessageType.INFO);

        assertTrue(infoHandlerToTest.supports(m));

    }

    @Test
    void supportsFalse(){

        for(MessageType mt : MessageType.values()){
            if(!mt.equals(MessageType.INFO)){
                Message m = mock(Message.class);
                when(m.getMessageType()).thenReturn(mt);

                assertFalse(infoHandlerToTest.supports(m));
            }

        }

    }

    @Test
    void handleSupported(){

        InfoMessage infoMessage = new InfoMessage();

        infoHandlerToTest.handle(infoMessage);

        verify(mirrorServiceMock, times(1)).testConnection();

    }

    @Test
    void handleUnsupported(){

        for(MessageType mt : MessageType.values()){
            if(!mt.equals(MessageType.INFO)){
                Message m = mock(Message.class);
                when(m.getMessageType()).thenReturn(mt);

                infoHandlerToTest.handle(m);

                verify(mirrorServiceMock, never()).testConnection();
            }

        }

    }

}