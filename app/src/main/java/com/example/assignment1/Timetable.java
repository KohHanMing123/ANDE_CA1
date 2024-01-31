package com.example.assignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Timetable extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference databaseReference;
    private ListView timetableListView;
    private Date selectedDate;
    private List<TimetableItem> timetableListByDay;
    private List<Exam> examListByDay;
    private boolean isTimetableSelected = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set the icon selected
        bottomNavigationView.setSelectedItemId(R.id.calendar);

        // Perform icon selected listener
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.home) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.homework) {
                    startActivity(new Intent(getApplicationContext(), HomeworkPage.class));
                    overridePendingTransition(0, 0);
                    return true;
                }  else if (itemId == R.id.time) {
                    startActivity(new Intent(getApplicationContext(), QuizPage.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.calendar) {
                    startActivity(new Intent(getApplicationContext(), Timetable.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.form) {
                    startActivity(new Intent(getApplicationContext(), ConsentFormListPage.class));
                    overridePendingTransition(0, 0);
                    return true;
                }

                return false;
            }
        });

        TextView headerMonthYear = findViewById(R.id.textHeaderMonthYear);
        TextView displayDate = findViewById(R.id.textViewDateDisplay);
        CalendarView calendar = findViewById(R.id.calendarView);

        // Initializing Month And Year
        Date date = new Date(calendar.getDate());
        selectedDate = new Date(calendar.getDate());
        SimpleDateFormat headerDisplay = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        SimpleDateFormat bottomDisplay = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault());

        String formattedDateForHeader = headerDisplay.format(date);
        String formattedDateForBtmDisplay = bottomDisplay.format(date);
        headerMonthYear.setText(formattedDateForHeader);
        displayDate.setText(formattedDateForBtmDisplay);

        //Intializing Timetable Items
        setUpTimetableListView(date);
        // When date changes
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate = new Date(year - 1900, month, dayOfMonth);
                displayDate.setText(bottomDisplay.format(selectedDate));
                headerMonthYear.setText(headerDisplay.format(selectedDate));
                if(isTimetableSelected){
                    setUpTimetableListView(selectedDate);
                }else{
                    setUpExamListView(selectedDate);
                }


            }
        });

    }

    @Override
    public void onClick(View v) {
        Button timetableBtn = findViewById(R.id.btnTimetable);
        Button examBtn = findViewById(R.id.btnExam);
        // Check which view was clicked
        if (v.getId() == R.id.btnTimetable) {

            timetableBtn.setBackground(getDrawable(R.drawable.orange_line));
            examBtn.setBackground(null);
            isTimetableSelected = true;
            setUpTimetableListView(selectedDate);
        }else{
            examBtn.setBackground(getDrawable(R.drawable.orange_line));
            timetableBtn.setBackground(null);
            isTimetableSelected = false;
            setUpExamListView(selectedDate);
        }
    }

    private void setUpExamListView(Date date){
        examListByDay = new ArrayList<>();
        SimpleDateFormat timetableComparison = new SimpleDateFormat("dd/MM/yyyy");
        String formattedTimetableComparisonDay = timetableComparison.format(date);
        String classname = User.class_name;
        databaseReference = FirebaseDatabase.getInstance().getReference("Exams").child(classname);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot examData : snapshot.getChildren()){
                    if(formattedTimetableComparisonDay.equals(examData.child("Date").getValue().toString())){
                        examListByDay.add(new Exam(examData.getKey().toString(), examData.child("Date").getValue().toString(), examData.child("Start_time").getValue().toString(), examData.child("End_time").getValue().toString(), examData.child("Venue").getValue().toString()));
                    }
                }
                renderExamListView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("a",error.toString());
            }
        });
    }
    private void renderExamListView(){
        //Exam shares timetable components
        timetableListView = findViewById(R.id.listTimetable);
        TextView textNoClasses = findViewById(R.id.textNoClasses);
        if(examListByDay.size() == 0){
            timetableListView.setVisibility(View.GONE);
            textNoClasses.setText("No Exams");
            textNoClasses.setVisibility(View.VISIBLE);
        }else{
            timetableListView.setVisibility(View.VISIBLE);
            textNoClasses.setVisibility(View.GONE);
        }
        ExamListAdapter examListAdapter = new ExamListAdapter(this, examListByDay);
        timetableListView.setAdapter(examListAdapter);
    }
    private void setUpTimetableListView(Date date) {
        timetableListByDay =  new ArrayList<>();
        SimpleDateFormat timetableComparison = new SimpleDateFormat("EEEE");
        String formattedTimetableComparisonDay = timetableComparison.format(date);
        String classname = User.class_name;
        databaseReference = FirebaseDatabase.getInstance().getReference("Timetable").child(classname);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for(DataSnapshot dayEventsData : snapshot.child(formattedTimetableComparisonDay).getChildren()){
                        String start_time = null, end_time = null, venue = null;
                        for(DataSnapshot eventData : dayEventsData.getChildren()){
                            switch (eventData.getKey().toString()){
                                case "Start_time":
                                    start_time = eventData.getValue().toString();
                                    break;
                                case "End_time":
                                    end_time = eventData.getValue().toString();
                                    break;
                                case "Venue":
                                    venue = eventData.getValue().toString();
                            }
                        }
                        if(start_time != null && end_time != null && venue != null){
                            timetableListByDay.add(new TimetableItem(dayEventsData.getKey().toString(), venue, start_time, end_time, "Monday"));

                        }
                    }
                renderTimetableListView(timetableListByDay);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("a",error.toString());
            }
        });
    }

    private List<TimetableItem> orderTimetable(List<TimetableItem> timetableItems){
        List<String> refTiming = new ArrayList<>();
        refTiming.add("0730");
        refTiming.add("0800");
        refTiming.add("0900");
        refTiming.add("1000");
        refTiming.add("1100");
        refTiming.add("1130");
        refTiming.add("1230");
        List<TimetableItem> tempTimetableItems = new ArrayList<>();

        for(int i = 0; i < timetableItems.size(); i++){
            for(TimetableItem timetableItem: timetableItems){
                if(timetableItem.getStart_time().equals(refTiming.get(i))){
                    tempTimetableItems.add(timetableItem);
                }
            }
        }

        return tempTimetableItems;
    }

    private void renderTimetableListView(List<TimetableItem> unprocessedTimetableItems){
        timetableListView = findViewById(R.id.listTimetable);
        TextView textNoClasses = findViewById(R.id.textNoClasses);
        if(unprocessedTimetableItems.size() == 0){
            timetableListView.setVisibility(View.GONE);
            textNoClasses.setText("No Classes");
            textNoClasses.setVisibility(View.VISIBLE);
        }else{
            timetableListView.setVisibility(View.VISIBLE);
            textNoClasses.setVisibility(View.GONE);
        }
        List<TimetableItem> timetableItems = orderTimetable(unprocessedTimetableItems);

        TimetableListAdapter timetableAdapter = new TimetableListAdapter(this, timetableItems);
        timetableListView.setAdapter(timetableAdapter);
    }
}