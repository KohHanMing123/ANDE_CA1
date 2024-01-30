package com.example.assignment1;

public class HomeworkItem {
    private final String homeworkTitle;
    private final String homeworkSubject;
    private final String homeworkDueDate;
    private boolean homeworkDone;
    public HomeworkItem(String homeworkTitle, String homeworkSubject, String homeworkDueDate, boolean homeworkDone) {
        this.homeworkTitle = homeworkTitle;
        this.homeworkSubject = homeworkSubject;
        this.homeworkDueDate = homeworkDueDate;
        this.homeworkDone = homeworkDone;
    }

    public String getHWTitle() {
        return this.homeworkTitle;
    }

    public String getHWSubject() {
        return this.homeworkSubject;
    }

    public String getHomeworkDueDate() {
        return homeworkDueDate;
    }

    public boolean getHomeworkDone() {
        return homeworkDone;
    }

    public void setHomeworkDone(boolean homeworkDone){
        this.homeworkDone = homeworkDone;
    }
}
