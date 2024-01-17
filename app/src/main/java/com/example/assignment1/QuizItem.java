package com.example.assignment1;

public class QuizItem {
    private String quizName;
    private String quizDescription;
    private String subject;


    public QuizItem(String quizName, String quizDescription, String subject) {
        this.quizName = quizName;
        this.quizDescription = quizDescription;
        this.subject = subject;
    }

    public String getQuizName() {
        return quizName;
    }

    public String getQuizDescription() {
        return quizDescription;
    }

    public String getSubject() { return subject; }
}