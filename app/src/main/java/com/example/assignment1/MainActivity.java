package com.example.assignment1;

import static java.lang.System.out;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    TextView textView;
    String[] listItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set the icon selected
        bottomNavigationView.setSelectedItemId(R.id.dashboard);

        // Perform icon selected listener
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.dashboard) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.time) {
                    startActivity(new Intent(getApplicationContext(), QuizPage.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.edit) {
                    startActivity(new Intent(getApplicationContext(), EditPage.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.settings) {
                    startActivity(new Intent(getApplicationContext(), SettingsPage.class));
                    overridePendingTransition(0, 0);
                    return true;
                }

                return false;
            }
        });
    }

    private void setupHomeworkListView() {
        listView = findViewById(R.id.listViewHW);

        List<HomeworkItem> homeworkItems = generateHWItems(); // method to create your list

//        QuizAdapter quizAdapter = new QuizAdapter(this, quizItems);
//        listView.setAdapter(quizAdapter);

    }

    private List<HomeworkItem> generateHWItems() {
        List<HomeworkItem> homeworkItems = new ArrayList<HomeworkItem>(){
            {
                add(new HomeworkItem("hello", "hello"));
            }
        };
        return homeworkItems;
    }
}
