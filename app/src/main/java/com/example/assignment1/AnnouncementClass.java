package com.example.assignment1;

import android.util.Log;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AnnouncementClass {

    private String author = "";
    private String category = "";
    private LocalDate date;
    private String dateString = "";

    private String title = "";

    public AnnouncementClass(String author, String category, String dateString, String title) {

        this.author = author;
        this.category = category;
        this.dateString = dateString;
        this.title = title;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");

        try {
            LocalDate localDate = LocalDate.parse(dateString, formatter);
            this.date = localDate;
        } catch (Exception e) {
            Log.e("DATE", e.getMessage());
        }
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
