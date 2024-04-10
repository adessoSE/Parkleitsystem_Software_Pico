package de.adesso.softwarepico.service;

import de.adesso.softwarepico.SoftwarePicoApplication;
import de.adesso.softwarepico.communication.Receiver;
import de.adesso.softwarepico.messageHandling.MessageFactory;
import de.adesso.softwarepico.messageHandling.handler.MessageHandler;
import de.adesso.softwarepico.messageHandling.message.Message;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MessageServiceTest {

    static Receiver receiverMock;
    static List<MessageHandler> messageHandlersMock;
    static MessageService messageServiceToTest;

    @BeforeAll
    @SuppressWarnings("unchecked")
    static void init(){
        receiverMock = mock(Receiver.class);
        messageHandlersMock = (List<MessageHandler>) mock(List.class);
        messageServiceToTest = spy(new MessageService(receiverMock, messageHandlersMock));
    }

    @BeforeEach
    void resetMocks(){
        reset(receiverMock, messageHandlersMock, messageServiceToTest);
    }

    @Test
    void subscribeToInbound(){

        String uuid = UUID.randomUUID().toString();

        MockedStatic<SoftwarePicoApplication> softwarePicoApplicationMock = mockStatic(SoftwarePicoApplication.class);

        softwarePicoApplicationMock.when(SoftwarePicoApplication::getUuid).thenReturn(uuid);

        messageServiceToTest.subscribeToInbound();

        verify(receiverMock, times(1)).subscribe(eq(uuid), any());
        verify(receiverMock, times(1)).subscribe(eq("software-pico"), any());

    }

    @Test
    void findSupportingMessageHandler(){

        Message m = mock(Message.class);
        MessageHandler h = mock(MessageHandler.class);
        when(messageHandlersMock.stream()).thenReturn(Stream.of(h));
        when(h.supports(m)).thenReturn(true);

        MessageHandler k = messageServiceToTest.findSupportingMessageHandler(m);

        assertEquals(h, k);
    }

    @Test
    void findSupportingMessageHandlerNotFound(){

        Message m = mock(Message.class);

        when(messageHandlersMock.stream()).thenReturn(Stream.empty());

        assertThrows(NoSuchElementException.class, () -> messageServiceToTest.findSupportingMessageHandler(m));
    }

    @Test
    void handle(){

        Message m = mock(Message.class);
        MessageHandler h = mock(MessageHandler.class);

        MockedStatic<MessageFactory> messageFactoryMock = mockStatic(MessageFactory.class);
        messageFactoryMock.when(() -> MessageFactory.fromJson(any())).thenReturn(m);

        doReturn(h).when(messageServiceToTest).findSupportingMessageHandler(m);


        messageServiceToTest.handle(new JSONObject());

        verify(h, times(1)).handle(m);

        messageFactoryMock.close();
    }

    @Test
    void handleInvalidMessage(){

        JSONObject jsonMessage = new JSONObject();

        MockedStatic<MessageFactory> messageFactoryMock = mockStatic(MessageFactory.class);
        messageFactoryMock.when(() -> MessageFactory.fromJson(any())).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class, () -> messageServiceToTest.handle(jsonMessage));

        messageFactoryMock.close();
    }


}