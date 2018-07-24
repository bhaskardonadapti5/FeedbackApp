package com.sample0.bhaskardondapati.feedbackapp;

import java.io.Serializable;
import java.util.ArrayList;

public class Question implements Serializable {

    String question;
    int answertype;
    boolean isQuestionAnswered;
    String userInputForQuestion;

    public boolean isQuestionAnswered() {
        return isQuestionAnswered;
    }

    public void setQuestionAnswered(boolean questionAnswered) {
        isQuestionAnswered = questionAnswered;
    }

    public String getUserInputForQuestion() {
        return userInputForQuestion;
    }

    public void setUserInputForQuestion(String userInputForQuestion) {
        this.userInputForQuestion = userInputForQuestion;
    }

    ArrayList<String> answers;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }



    public int getAnswertype() {
        return answertype;
    }

    public void setAnswertype(int answertype) {
        this.answertype = answertype;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }
}
