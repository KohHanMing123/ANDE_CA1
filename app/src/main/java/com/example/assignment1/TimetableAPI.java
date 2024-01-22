package com.example.assignment1;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TimetableAPI {
    private DatabaseReference databaseReference;
    TimetableAPICallback apiCallback;

    public TimetableAPI(){

    }
    public  void setApiCallback(TimetableAPICallback apiCallback){
        this.apiCallback= apiCallback;
    }
    public void getTimetableItemsByDay(Date date){
System.out.println(date);
        List<TimetableItem> timetableListByDay = new ArrayList<TimetableItem>();
        SimpleDateFormat timetableComparison = new SimpleDateFormat("EEEE");
        String formattedTimetableComparisonDay = timetableComparison.format(date);
        String classname = "1B";
        databaseReference = FirebaseDatabase.getInstance().getReference("Timetable").child(classname);

        List<String> listOfDays = new ArrayList<String>( Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"));
        List<String> listOfEvents = new ArrayList<String>( Arrays.asList("English", "Math", "Mother Tongue", "Science", "Assembly", "Art", "Physical Education", "Teacher Time", "Recess"));
        for(String eventToCheck : listOfEvents){
            databaseReference.child(formattedTimetableComparisonDay).child(eventToCheck).get().addOnCompleteListener(task -> {
                String start_time = null, end_time = null, venue = null;
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    for(DataSnapshot lmao : task.getResult().getChildren()){

                        switch (lmao.getKey().toString()){
                            case "Start_time":
                                start_time = lmao.getValue().toString();
                                break;
                            case "End_time":
                                end_time = lmao.getValue().toString();
                                break;
                            case "Venue":
                                venue = lmao.getValue().toString();
                        }

                    }
                    if(start_time != null && venue != null && end_time != null){
                        TimetableItem newTimeTableItem = new TimetableItem(eventToCheck, venue, start_time, end_time, "Monday");

                        timetableListByDay.add(newTimeTableItem);
                    }
                }
            });
        }
        apiCallback.displayTimetable(timetableListByDay);
    }

    public interface TimetableAPICallback{
        void displayTimetable(List<TimetableItem> timetableItems);
    }
}
