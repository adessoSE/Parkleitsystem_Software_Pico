package de.adesso.softwarepico.messageHandling.handler;

import de.adesso.softwarepico.messageHandling.SoftwarePicoMessageType;
import de.adesso.communication.messageHandling.Message;
import de.adesso.softwarepico.messageHandling.message.ReservationMessage;
import de.adesso.softwarepico.service.mirror.MirrorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationHandlerTest {

    static MirrorService mirrorServiceMock;
    static ReservationHandler reservationHandlerToTest;

    @BeforeAll
    static void init(){
        mirrorServiceMock = mock(MirrorService.class);
        reservationHandlerToTest = new ReservationHandler(mirrorServiceMock);
    }

    @BeforeEach
    void resetMocks(){
        reset(mirrorServiceMock);
    }

    @Test
    void supportsTrue(){

        Message m = mock(Message.class);

        when(m.getMessageType()).thenReturn(SoftwarePicoMessageType.RESERVE);

        assertTrue(reservationHandlerToTest.supports(m));

    }

    @Test
    void supportsFalse(){

        for(SoftwarePicoMessageType mt : SoftwarePicoMessageType.values()){
            if(!mt.equals(SoftwarePicoMessageType.RESERVE)){
                Message m = mock(Message.class);
                when(m.getMessageType()).thenReturn(mt);

                assertFalse(reservationHandlerToTest.supports(m));
            }

        }

    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void handleSupported(boolean reserve){

        ReservationMessage reservationMessage = new ReservationMessage(reserve);

        reservationHandlerToTest.handle(reservationMessage);

        verify(mirrorServiceMock, times(1)).changeReservationStatus(reserve);

    }

    @Test
    void handleUnsupported(){

        for(SoftwarePicoMessageType mt : SoftwarePicoMessageType.values()){
            if(!mt.equals(SoftwarePicoMessageType.RESERVE)){
                Message m = mock(Message.class);
                when(m.getMessageType()).thenReturn(mt);

                reservationHandlerToTest.handle(m);

                verify(mirrorServiceMock, never()).changeReservationStatus(anyBoolean());
            }

        }

    }

}