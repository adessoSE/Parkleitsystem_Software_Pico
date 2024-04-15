package de.adesso.softwarepico.messageHandling.handler;

import de.adesso.communication.messageHandling.MessageHandler;
import de.adesso.softwarepico.messageHandling.SoftwarePicoMessageType;
import de.adesso.communication.messageHandling.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class HandlerSupportTest {

    @Autowired
    List<MessageHandler> messageHandlers;

    @Test
    void allTypesSupported(){
        for(SoftwarePicoMessageType mt : SoftwarePicoMessageType.values()){
            Message m = () -> mt;
            boolean supported = false;
            for(MessageHandler h : messageHandlers){
                supported = supported || h.supports(m);
            }
            assertTrue(supported);
        }
    }

}
