package de.adesso.softwarepico.messageHandling.handler;

import de.adesso.softwarepico.messageHandling.MessageType;
import de.adesso.softwarepico.messageHandling.message.Message;
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
        for(MessageType mt : MessageType.values()){
            Message m = () -> mt;
            boolean supported = false;
            for(MessageHandler h : messageHandlers){
                supported = supported || h.supports(m);
            }
            assertTrue(supported);
        }
    }

}
