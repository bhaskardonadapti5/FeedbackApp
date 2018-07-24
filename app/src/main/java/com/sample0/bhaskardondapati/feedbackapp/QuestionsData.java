package com.sample0.bhaskardondapati.feedbackapp;

import java.io.Serializable;
import java.util.ArrayList;

public class QuestionsData implements Serializable {

  ArrayList<Question> questions;

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }
}
