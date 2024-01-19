package com.example.assignment1;

import java.util.List;

public class Question {
    private String questionTitle;
    private List<String> options;
    private boolean[] isOptionCorrect;


    public Question(String questionTitle, List<String> options, boolean[] isOptionCorrect) {
        this.questionTitle = questionTitle;
        this.options = options;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public List<String> getOptions() {
        return options;
    }

    public boolean[] getIsOptionCorrect() {
        return isOptionCorrect;
    }
}

