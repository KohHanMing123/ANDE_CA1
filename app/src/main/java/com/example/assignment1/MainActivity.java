package com.example.assignment1;

import static java.lang.System.out;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    TextView textView, announcementTextView;
    ImageSwitcher announcementImageSwitcher;
    private int[] announcementImages = {R.drawable.books, R.drawable.assembly_announcement, R.drawable.recess_party};
    private String[] announcementText = {"National Book Day on 25 November!", "Joakim & Sonia this Wednesday!", "Recess Party on 1 December!"};
    private int currentIndex = 0;
    private Handler handler = new Handler();
    String[] listItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println(String.valueOf(R.string.firebase_url));
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setDatabaseUrl("https://andeca2-b5af6-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .setApplicationId("1:590903525065:android:1b622cff39e82e8a631718")
                .setApiKey("AIzaSyDmoAYjWVoJmz0JnSagYf_qMppU93I6bKc")
                .setProjectId("andeca2-b5af6")
                .build();

        FirebaseApp.initializeApp(this, options);
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set the icon selected
        bottomNavigationView.setSelectedItemId(R.id.home);

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

//        ANIMATION PROPERTIES
        ImageSwitcher announcementsImages = (ImageSwitcher) findViewById(R.id.imageSwitcherAnnouncement);
        Animation slide = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.side_slide);
        Animation flies = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.flies_up);
        listView = findViewById(R.id.listViewHW);
        announcementsImages.startAnimation(slide);
        listView.startAnimation(slide);
        TextView welcomeText = (TextView) findViewById(R.id.textWelcome);
        welcomeText.startAnimation(flies);
        // END OF ANIMATION


        announcementImageSwitcher = findViewById(R.id.imageSwitcherAnnouncement);
        announcementTextView = findViewById(R.id.textViewAnnouncement);
        announcementImageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView myView = new ImageView(getApplicationContext());
                myView.setScaleType(ImageView.ScaleType.FIT_XY);


                return myView;
            }
        });
        announcementImageSwitcher.setInAnimation(AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_in));
        announcementImageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_out));
        announcementImageSwitcher.setImageResource(announcementImages[currentIndex]);
        announcementTextView.setText(announcementText[currentIndex]);
        long interval = 3000L; // 3 seconds
        handler.postDelayed(announcementSwitchRunnable, interval);
        setupHomeworkListView();
        generateHWItems();
    }

    private void setupHomeworkListView() {
        listView = findViewById(R.id.listViewHW);

        List<HomeworkItem> homeworkItems = generateHWItems(); // method to create your list

        HomeworkListAdapter hwAdapter = new HomeworkListAdapter(this, homeworkItems);
        listView.setAdapter(hwAdapter);

    }

    private List<HomeworkItem> generateHWItems() {
        List<HomeworkItem> homeworkItems = new ArrayList<HomeworkItem>(){
            {
                add(new HomeworkItem("Daily Problem Sums", "Mathematics"));
                add(new HomeworkItem("Workbook Page 31-33", "Chinese"));
                add(new HomeworkItem("Exercise 3B", "Science"));
                add(new HomeworkItem("Learn Spelling", "English"));
            }
        };
        return homeworkItems;
    }

    private Runnable announcementSwitchRunnable = new Runnable() {
        @Override
        public void run() {
            // Update index and switch image
            currentIndex = (currentIndex + 1) % announcementImages.length;
            announcementImageSwitcher.setImageResource(announcementImages[currentIndex]);
            announcementTextView.setText(announcementText[currentIndex]);


            long interval = 3000L; // 3 seconds
            handler.postDelayed(this, interval);
        }
    };


}
