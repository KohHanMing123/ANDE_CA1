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

public class QuizPage extends AppCompatActivity {
    ListView listView;
    TextView textView;
    String[] listItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_page);
        Log.d("CurrentPage", "iM INSIDE QuizPage.java");

        // Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.time);

        // Perform item selected listener
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.dashboard) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.time) {
                    // Call a method to set up the ListView
                    setupQuizListView();
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
        setupQuizListView();
        generateQuizItems();
    }
        // Method to set up the ListView
        private void setupQuizListView() {
            listView = findViewById(R.id.listViewQuizzes);
            Log.d("QuizListView", "Inside listView method"); // Log a message

            List<QuizItem> quizItems = generateQuizItems(); // method to create your list
            Log.d("QuizListView", "QuizItems: " + quizItems); // Log quizItems

            QuizAdapter quizAdapter = new QuizAdapter(this, quizItems);
            listView.setAdapter(quizAdapter);
            Log.d("QuizListView", "ListView: " + listView); // Log listView

        }

    private List<QuizItem> generateQuizItems() {
        Log.d("QuizPage", "Inside generateQuizItems method");

        List<QuizItem> quizItems = new ArrayList<>();

        String quizName1 = getString(R.string.quiz_name_1);
        String subtext1 = getString(R.string.subtext_1);
        quizItems.add(new QuizItem(quizName1, subtext1));

        String quizName2 = getString(R.string.quiz_name_2);
        String subtext2 = getString(R.string.subtext_2);
        quizItems.add(new QuizItem(quizName2, subtext2));

        String quizName3 = getString(R.string.quiz_name_3);
        String subtext3 = getString(R.string.subtext_3);
        quizItems.add(new QuizItem(quizName3, subtext3));

        String quizName4 = getString(R.string.quiz_name_4);
        String subtext4 = getString(R.string.subtext_4);
        quizItems.add(new QuizItem(quizName4, subtext4));

        String quizName5 = getString(R.string.quiz_name_5);
        String subtext5 = getString(R.string.subtext_5);
        quizItems.add(new QuizItem(quizName5, subtext5));


        Log.d("QuizItemsRes", "QuizItems: " + quizItems);

        return quizItems;
    }
}


