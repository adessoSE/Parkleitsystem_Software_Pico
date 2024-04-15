package de.adesso.softwarepico.service;

import de.adesso.softwarepico.SoftwarePicoApplication;
import de.adesso.communication.messaging.Receiver;
import de.adesso.softwarepico.messageHandling.SoftwarePicoMessageFactory;
import de.adesso.communication.messageHandling.MessageHandler;
import de.adesso.communication.messageHandling.Message;
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

class SubscribingServiceTest {

    static Receiver receiverMock;
    static List<MessageHandler> messageHandlersMock;
    static SubscribingService subscribingServiceToTest;

    @BeforeAll
    @SuppressWarnings("unchecked")
    static void init(){
        receiverMock = mock(Receiver.class);
        messageHandlersMock = (List<MessageHandler>) mock(List.class);
        subscribingServiceToTest = spy(new SubscribingService(receiverMock, messageHandlersMock));
    }

    @BeforeEach
    void resetMocks(){
        reset(receiverMock, messageHandlersMock, subscribingServiceToTest);
    }

    @Test
    void subscribeToInbound(){

        String uuid = UUID.randomUUID().toString();

        MockedStatic<SoftwarePicoApplication> softwarePicoApplicationMock = mockStatic(SoftwarePicoApplication.class);

        softwarePicoApplicationMock.when(SoftwarePicoApplication::getUuid).thenReturn(uuid);

        subscribingServiceToTest.subscribeToInbound();

        verify(receiverMock, times(1)).subscribe(eq(uuid), any());
        verify(receiverMock, times(1)).subscribe(eq("software-pico"), any());

    }

    @Test
    void findSupportingMessageHandler(){

        Message m = mock(Message.class);
        MessageHandler h = mock(MessageHandler.class);
        when(messageHandlersMock.stream()).thenReturn(Stream.of(h));
        when(h.supports(m)).thenReturn(true);

        MessageHandler k = subscribingServiceToTest.findSupportingMessageHandler(m);

        assertEquals(h, k);
    }

    @Test
    void findSupportingMessageHandlerNotFound(){

        Message m = mock(Message.class);

        when(messageHandlersMock.stream()).thenReturn(Stream.empty());

        assertThrows(NoSuchElementException.class, () -> subscribingServiceToTest.findSupportingMessageHandler(m));
    }

    @Test
    void handle(){

        Message m = mock(Message.class);
        MessageHandler h = mock(MessageHandler.class);

        MockedStatic<SoftwarePicoMessageFactory> messageFactoryMock = mockStatic(SoftwarePicoMessageFactory.class);
        messageFactoryMock.when(() -> SoftwarePicoMessageFactory.fromJson(any())).thenReturn(m);

        doReturn(h).when(subscribingServiceToTest).findSupportingMessageHandler(m);


        subscribingServiceToTest.handle(new JSONObject());

        verify(h, times(1)).handle(m);

        messageFactoryMock.close();
    }

    @Test
    void handleInvalidMessage(){

        JSONObject jsonMessage = new JSONObject();

        MockedStatic<SoftwarePicoMessageFactory> messageFactoryMock = mockStatic(SoftwarePicoMessageFactory.class);
        messageFactoryMock.when(() -> SoftwarePicoMessageFactory.fromJson(any())).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class, () -> subscribingServiceToTest.handle(jsonMessage));

        messageFactoryMock.close();
    }


}