package com.example.assignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class QuizPage extends AppCompatActivity {

    ListView listView;
    private TabLayout tabLayout;
    TextView textView;
    String[] listItem;
    private DatabaseReference databaseReference;
    List<QuizItem> quizItems;


    private Set<String> uniqueSubjects = new HashSet<>();
    private Map<String, List<QuizItem>> quizzesBySubject = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_page);
        Log.d("CurrentPage", "iM INSIDE QuizPage.java");

        listView = findViewById(R.id.listViewQuizzes);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QuizItem clickedQuiz = (QuizItem) parent.getItemAtPosition(position);

                Intent intent = new Intent(QuizPage.this, QuizDetail.class);
                intent.putExtra("quizTitle", clickedQuiz.getQuizName());

                startActivity(intent);
            }
        });


        quizItems = new ArrayList<>();
        // Your Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Quiz");

        // Add a listener to fetch data
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uniqueSubjects.clear();
                quizzesBySubject.clear();

                for (DataSnapshot subjectSnapshot : dataSnapshot.getChildren()) {
                    String subjectName = subjectSnapshot.getKey(); // Get subject name
                    uniqueSubjects.add(subjectName);
                    Log.d("FirebaseData", "Subject Name: " + subjectName);

                    List<QuizItem> subjectQuizzes = new ArrayList<>();

                    for (DataSnapshot quizSnapshot : subjectSnapshot.getChildren()) {
                        String quizTitle = quizSnapshot.child("quiz_title").getValue(String.class);
                        String quizDescription = quizSnapshot.child("quiz_description").getValue(String.class);

                        Log.d("FirebaseData", "Quiz Title: " + quizTitle);
                        Log.d("FirebaseData", "Quiz Description: " + quizDescription);

                        subjectQuizzes.add(new QuizItem(quizTitle, quizDescription, subjectName));
                    }

                    Log.d("FirebaseData", "Quizzes for Subject " + subjectName + ": " + subjectQuizzes);

                    quizzesBySubject.put(subjectName, subjectQuizzes);
                }
                Log.d("hello", String.valueOf(quizzesBySubject.keySet()));

                updateUI(quizzesBySubject, uniqueSubjects);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("FirebaseData", "Failed to read value.", databaseError.toException());
            }
        });

        // Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.time);

        // Perform item selected listener
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
                } else if (itemId == R.id.time) {
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


        // Initialize TabLayout
        tabLayout = findViewById(R.id.tabLayoutSubjects);

        // Set tab click listener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));

                TextView tabText = tab.getCustomView().findViewById(R.id.customTabText);

                if (tabText != null) {
                    tabText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                }

                String selectedSubject = tabText.getText().toString();
                Log.d("selected subject is", selectedSubject);

                Log.d("FirebaseData", "All subjects in quizzesBySubject: " + quizzesBySubject.keySet());

                if (quizzesBySubject.containsKey(selectedSubject)) {
                    QuizAdapter quizAdapter = new QuizAdapter(getApplicationContext(), quizzesBySubject.get(selectedSubject));
                    listView.setAdapter(quizAdapter);
                } else {
                    Log.e("FirebaseData", "No data found for subject: " + selectedSubject);
                }

                updateTabLayout(tabLayout, uniqueSubjects);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.background));

                TextView tabText = tab.getCustomView().findViewById(R.id.customTabText);

                if (tabText != null) {
                    tabText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // could be used
            }
        });

//        updateQuizListView(quizItems);
//        generateQuizItems();
    }

    private void updateTabLayout(TabLayout tabLayout, Set<String> uniqueSubjects) {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                View tabView = tab.view;
                if (tab.isSelected()) {
                    // Apply rounded corners for the selected tab
                    tabView.setBackgroundResource(R.drawable.rounded_tab_layout_background);
                } else {
                    tabView.setBackgroundResource(android.R.color.transparent);
                }
            }
        }
    }
    private void createTabs(Set<String> uniqueSubjects) {
        tabLayout.removeAllTabs();

        for (String subject : uniqueSubjects) {
            TabLayout.Tab tab = tabLayout.newTab();

            View customTabView = LayoutInflater.from(this).inflate(R.layout.custom_tab_item, null);

            TextView textView = customTabView.findViewById(R.id.customTabText);
            Log.d("custom tab subject", subject);
            textView.setText(subject);

            tab.setCustomView(customTabView);

            tabLayout.addTab(tab);
        }
    }

    private void updateUI(Map<String, List<QuizItem>> quizzesBySubject, Set<String> uniqueSubjects) {
        updateTabLayout(tabLayout, uniqueSubjects);
        createTabs(uniqueSubjects);

        // Choose a default subject or implement logic to select a subject
        String selectedSubject = uniqueSubjects.iterator().next();

        setupQuizListView(quizzesBySubject.get(selectedSubject));
    }


    private void setupQuizListView(List<QuizItem> quizItems) {
        Log.d("setupquizlistview","Setting up QuizListView with " + quizItems.size() + " items");

        listView = findViewById(R.id.listViewQuizzes);

        if (listView != null) {
            QuizAdapter quizAdapter = new QuizAdapter(this, quizItems);
            listView.setAdapter(quizAdapter);
        } else {
            Log.e("ListViewError", "ListView is null. Make sure it is properly initialized in your layout.");
        }
    }
}