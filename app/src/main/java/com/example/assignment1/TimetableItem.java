package com.example.assignment1;

public class TimetableItem {
    private String event, venue, start_time, end_time, day;

    public TimetableItem( String event, String venue, String start_time, String end_time, String day){
        this.event = event;
        this.venue  = venue;
        this.start_time = start_time;
        this.end_time = end_time;
        this.day = day;
    }


    public String getEvent() {
        return event;
    }

    public String getVenue() {
        return venue;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getDay() {
        return day;
    }
}
