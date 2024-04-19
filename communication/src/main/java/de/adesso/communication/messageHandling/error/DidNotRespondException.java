package de.adesso.communication.messageHandling.error;

import de.adesso.communication.messageHandling.answerChecking.Answer;

public class DidNotRespondException extends RuntimeException{

    private final Answer expected;


    public DidNotRespondException(Answer expected) {
        this.expected = expected;
    }

    public Answer getExpected() {
        return expected;
    }
}
