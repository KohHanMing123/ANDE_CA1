package com.example.assignment1;

public class HomeworkItem {
    private String homeworkTitle, homeworkSubject;

    public HomeworkItem(String homeworkTitle, String homeworkSubject) {
        this.homeworkTitle = homeworkTitle;
        this.homeworkSubject = homeworkSubject;
    }

    public String getHWTitle() {
        return this.homeworkTitle;
    }

    public String getHWSubject() {
        return this.homeworkSubject;
    }
}
