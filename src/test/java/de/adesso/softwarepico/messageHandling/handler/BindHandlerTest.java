package de.adesso.softwarepico.messageHandling.handler;

import de.adesso.softwarepico.messageHandling.SoftwarePicoMessageType;
import de.adesso.softwarepico.messageHandling.message.BindMessage;
import de.adesso.communication.messageHandling.Message;
import de.adesso.softwarepico.service.mirror.MirrorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BindHandlerTest {

    static MirrorService mirrorServiceMock;
    static BindHandler bindHandlerToTest;

    @BeforeAll
    static void init(){
        mirrorServiceMock = mock(MirrorService.class);
        bindHandlerToTest = new BindHandler(mirrorServiceMock);
    }

    @BeforeEach
    void resetMocks(){
        reset(mirrorServiceMock);
    }

    @Test
    void supportsTrue(){

        Message m = mock(Message.class);

        when(m.getMessageType()).thenReturn(SoftwarePicoMessageType.BIND);

        assertTrue(bindHandlerToTest.supports(m));

    }

    @Test
    void supportsFalse(){

        for(SoftwarePicoMessageType mt : SoftwarePicoMessageType.values()){
            if(!mt.equals(SoftwarePicoMessageType.BIND)){
                Message m = mock(Message.class);
                when(m.getMessageType()).thenReturn(mt);

                assertFalse(bindHandlerToTest.supports(m));
            }

        }

    }

    @Test
    void handleSupported(){

        int hardwarePicoId = 1;
        String hardwarePicoIp = "abc";

        BindMessage bindMessage = new BindMessage(hardwarePicoId, hardwarePicoIp);

        bindHandlerToTest.handle(bindMessage);

        verify(mirrorServiceMock, times(1)).initialize(hardwarePicoId, hardwarePicoIp);

    }

    @Test
    void handleUnsupported(){

        for(SoftwarePicoMessageType mt : SoftwarePicoMessageType.values()){
            if(!mt.equals(SoftwarePicoMessageType.BIND)){
                Message m = mock(Message.class);
                when(m.getMessageType()).thenReturn(mt);

                bindHandlerToTest.handle(m);

                verify(mirrorServiceMock, never()).initialize(anyInt(), anyString());
            }

        }

    }

}