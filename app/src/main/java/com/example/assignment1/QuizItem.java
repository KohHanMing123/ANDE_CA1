package com.example.assignment1;

public class QuizItem {
    private String quizName;
    private String subtext;

    public QuizItem(String quizName, String subtext) {
        this.quizName = quizName;
        this.subtext = subtext;
    }

    public String getQuizName() {
        return quizName;
    }

    public String getSubtext() {
        return subtext;
    }
}
