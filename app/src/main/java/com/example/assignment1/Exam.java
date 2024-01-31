package com.example.assignment1;

public class Exam {
    private String title, date, startTime, endTime , venue;

    public Exam(String title, String date, String startTime, String endTime, String venue) {
        this.title = title;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.venue = venue;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getVenue(){
        return  venue;
    }
}
