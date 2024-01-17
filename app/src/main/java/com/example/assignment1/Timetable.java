package com.example.assignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Timetable extends AppCompatActivity implements View.OnClickListener {
    private long selectedDate = -1;
    private GestureDetector gestureDetector;
    @SuppressLint("ClickableViewAccessibility")
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
                    startActivity(new Intent(getApplicationContext(), SettingsPage.class));
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
        SimpleDateFormat headerDisplay = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        SimpleDateFormat bottomDisplay = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault());

        String formattedDateForHeader = headerDisplay.format(date);
        String formattedDateForBtmDisplay = bottomDisplay.format(date);
        headerMonthYear.setText(formattedDateForHeader);
        displayDate.setText(formattedDateForBtmDisplay);
        // When date changes
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Date selectedDate = new Date(year - 1900, month, dayOfMonth);

                displayDate.setText(bottomDisplay.format(selectedDate));

                String monthString = new DateFormatSymbols().getMonths()[month];

               headerMonthYear.setText(headerDisplay.format(selectedDate));
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

        }else{
            examBtn.setBackground(getDrawable(R.drawable.orange_line));
            timetableBtn.setBackground(null);
        }
    }
}