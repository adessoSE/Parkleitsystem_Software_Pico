package de.adesso.softwarepico.messageHandling.handler;

import de.adesso.softwarepico.configuration.SensorStatus;
import de.adesso.softwarepico.messageHandling.SoftwarePicoMessageType;
import de.adesso.communication.messageHandling.Message;
import de.adesso.softwarepico.messageHandling.message.SensorInfoMessage;
import de.adesso.softwarepico.service.mirror.MirrorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SensorInfoHandlerTest {

    static MirrorService mirrorServiceMock;
    static SensorInfoHandler sensorInfoHandlerToTest;

    @BeforeAll
    static void init(){
        mirrorServiceMock = mock(MirrorService.class);
        sensorInfoHandlerToTest = new SensorInfoHandler(mirrorServiceMock);
    }

    @BeforeEach
    void resetMocks(){
        reset(mirrorServiceMock);
    }

    @Test
    void supportsTrue(){

        Message m = mock(Message.class);

        when(m.getMessageType()).thenReturn(SoftwarePicoMessageType.SENSOR_INFO);

        assertTrue(sensorInfoHandlerToTest.supports(m));

    }

    @Test
    void supportsFalse(){

        for(SoftwarePicoMessageType mt : SoftwarePicoMessageType.values()){
            if(!mt.equals(SoftwarePicoMessageType.SENSOR_INFO)){
                Message m = mock(Message.class);
                when(m.getMessageType()).thenReturn(mt);

                assertFalse(sensorInfoHandlerToTest.supports(m));
            }

        }

    }

    @ParameterizedTest
    @ValueSource(strings = {"free", "blocked", "defect", "unknown"})
    void handleSupported(String status){

        SensorInfoMessage sensorInfoMessage = new SensorInfoMessage(status);

        sensorInfoHandlerToTest.handle(sensorInfoMessage);

        verify(mirrorServiceMock, times(1)).setSensorStatus(SensorStatus.valueOf(status.toUpperCase()));

    }

    @Test
    void handleUnsupported(){

        for(SoftwarePicoMessageType mt : SoftwarePicoMessageType.values()){
            if(!mt.equals(SoftwarePicoMessageType.SENSOR_INFO)){
                Message m = mock(Message.class);
                when(m.getMessageType()).thenReturn(mt);

                sensorInfoHandlerToTest.handle(m);

                verify(mirrorServiceMock, never()).setSensorStatus(any(SensorStatus.class));
            }

        }

    }

}