package de.adesso.communication.messageHandling.error;

import de.adesso.communication.messageHandling.answerChecking.Answer;

public class AnswerAfterTimeoutException extends RuntimeException{

    private final Answer answer;

    public AnswerAfterTimeoutException(Answer answer){
        super();
        this.answer = answer;
    }

    public Answer getAnswer() {
        return answer;
    }
}
