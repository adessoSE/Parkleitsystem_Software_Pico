package de.adesso.softwarepico.messageHandling.handler;

import de.adesso.softwarepico.messageHandling.MessageType;
import de.adesso.softwarepico.messageHandling.message.Message;
import de.adesso.softwarepico.messageHandling.message.RebindMessage;
import de.adesso.softwarepico.service.mirror.MirrorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class RebindHandlerTest {

    static MirrorService mirrorServiceMock;
    static RebindHandler rebindHandlerToTest;

    @BeforeAll
    static void init(){
        mirrorServiceMock = mock(MirrorService.class);
        rebindHandlerToTest = new RebindHandler(mirrorServiceMock);
    }

    @BeforeEach
    void resetMocks(){
        reset(mirrorServiceMock);
    }

    @Test
    void supportsTrue(){

        Message m = mock(Message.class);

        when(m.getMessageType()).thenReturn(MessageType.REBIND);

        assertTrue(rebindHandlerToTest.supports(m));

    }

    @Test
    void supportsFalse(){

        for(MessageType mt : MessageType.values()){
            if(!mt.equals(MessageType.REBIND)){
                Message m = mock(Message.class);
                when(m.getMessageType()).thenReturn(mt);

                assertFalse(rebindHandlerToTest.supports(m));
            }

        }

    }

    @Test
    void handleSupported(){

        String hardwarePicoIp = "abc";

        RebindMessage rebindMessage = new RebindMessage(hardwarePicoIp);

        rebindHandlerToTest.handle(rebindMessage);

        verify(mirrorServiceMock, times(1)).rebind(hardwarePicoIp);

    }

    @Test
    void handleUnsupported(){

        for(MessageType mt : MessageType.values()){
            if(!mt.equals(MessageType.REBIND)){
                Message m = mock(Message.class);
                when(m.getMessageType()).thenReturn(mt);

                rebindHandlerToTest.handle(m);

                verify(mirrorServiceMock, never()).rebind(anyString());
            }

        }

    }

}