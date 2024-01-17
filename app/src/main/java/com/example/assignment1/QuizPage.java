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


    private final Set<String> uniqueSubjects = new HashSet<>();
    private final Map<String, List<QuizItem>> quizzesBySubject = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_page);
        Log.d("CurrentPage", "iM INSIDE QuizPage.java");

        // Initialize listView
        listView = findViewById(R.id.listViewQuizzes);


        quizItems = new ArrayList<>();
        // Your Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Quiz");

        // Add a listener to fetch data
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uniqueSubjects.clear(); // Clear the set before adding new items

                // Map to store quizzes organized by subjects
                quizzesBySubject.clear(); // Clear the class-level variable

                for (DataSnapshot subjectSnapshot : dataSnapshot.getChildren()) {
                    String subjectName = subjectSnapshot.getKey(); // Get subject name
                    uniqueSubjects.add(subjectName);
                    Log.d("FirebaseData", "Subject Name: " + subjectName);

                    List<QuizItem> subjectQuizzes = new ArrayList<>();

                    for (DataSnapshot quizSnapshot : subjectSnapshot.getChildren()) {
                        String quizTitle = quizSnapshot.child("quiz_title").getValue(String.class);
                        String quizDescription = quizSnapshot.child("quiz_description").getValue(String.class);

                        // Log the data
                        Log.d("FirebaseData", "Quiz Title: " + quizTitle);
                        Log.d("FirebaseData", "Quiz Description: " + quizDescription);

                        subjectQuizzes.add(new QuizItem(quizTitle, quizDescription, subjectName));
                    }

                    // Log the quizzes within the subject
                    Log.d("FirebaseData", "Quizzes for Subject " + subjectName + ": " + subjectQuizzes);

                    // Add quizzes for the subject to the map
                    quizzesBySubject.put(subjectName, subjectQuizzes);
                }
                Log.d("hello", String.valueOf(quizzesBySubject.keySet()));

                // Update the UI with the retrieved data
                updateUI(quizzesBySubject, uniqueSubjects);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Failed to read value
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
                }  else if (itemId == R.id.time) {
                    startActivity(new Intent(getApplicationContext(), QuizPage.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.calendar) {
                    startActivity(new Intent(getApplicationContext(), EditPage.class));
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


        // Initialize TabLayout
        tabLayout = findViewById(R.id.tabLayoutSubjects);

        // Set tab click listener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Change background color and text color when tab is selected
                tab.view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));

                // Get the TextView from the custom layout of the selected tab
                TextView tabText = tab.getCustomView().findViewById(R.id.customTabText);

                if (tabText != null) {
                    tabText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                }

                // Update quiz list for the selected subject
                String selectedSubject = tabText.getText().toString();
                Log.d("selected subject is", selectedSubject);

                // Log the contents of quizzesBySubject for debugging purposes
                Log.d("FirebaseData", "All subjects in quizzesBySubject: " + quizzesBySubject.keySet());

                if (quizzesBySubject.containsKey(selectedSubject)) {
                    // Create a new instance of QuizAdapter every time the tab is selected
                    QuizAdapter quizAdapter = new QuizAdapter(getApplicationContext(), quizzesBySubject.get(selectedSubject));
                    listView.setAdapter(quizAdapter);
                } else {
                    Log.e("FirebaseData", "No data found for subject: " + selectedSubject);
                }

                updateTabLayout(tabLayout, uniqueSubjects);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Change background color and text color of the unselected tab to the default color
                tab.view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.background));

                // Get the TextView from the custom layout of the unselected tab
                TextView tabText = tab.getCustomView().findViewById(R.id.customTabText);

                if (tabText != null) {
                    tabText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // You may choose to handle reselection differently if needed
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
        // Clear existing tabs
        tabLayout.removeAllTabs();

        for (String subject : uniqueSubjects) {
            // Create a new tab
            TabLayout.Tab tab = tabLayout.newTab();

            // Inflate the custom_tab_item.xml layout
            View customTabView = LayoutInflater.from(this).inflate(R.layout.custom_tab_item, null);

            // Find the TextView in the custom layout and set its text
            TextView textView = customTabView.findViewById(R.id.customTabText);
            Log.d("custom tab subject", subject);
            textView.setText(subject);

            // Set the custom view for the tab
            tab.setCustomView(customTabView);

            // Add the tab to the TabLayout
            tabLayout.addTab(tab);
        }
    }

    // Update the UI with the retrieved data
    private void updateUI(Map<String, List<QuizItem>> quizzesBySubject, Set<String> uniqueSubjects) {
        // Update the tab layout
        updateTabLayout(tabLayout, uniqueSubjects);
        createTabs(uniqueSubjects);

        // Choose a default subject or implement logic to select a subject
        String selectedSubject = uniqueSubjects.iterator().next();

        // Update the quiz list view for the default subject
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
    // Modify the method to update the QuizAdapter and notify the changes
//    private void updateQuizListView(List<QuizItem> quizItems) {
//        QuizAdapter quizAdapter = (QuizAdapter) listView.getAdapter();
//        if (quizAdapter != null) {
//            quizAdapter.updateData(quizItems);
//        } else {
//            quizAdapter = new QuizAdapter(this, quizItems);
//            listView.setAdapter(quizAdapter);
//        }
//    }

//    private List<QuizItem> generateQuizItems() {
//        Log.d("QuizPage", "Inside generateQuizItems method");
//
//        List<QuizItem> quizItems = new ArrayList<>();
//
//        String quizName1 = getString(R.string.quiz_name_1);
//        String subtext1 = getString(R.string.subtext_1);
//        quizItems.add(new QuizItem(quizName1, subtext1));
//
//        String quizName2 = getString(R.string.quiz_name_2);
//        String subtext2 = getString(R.string.subtext_2);
//        quizItems.add(new QuizItem(quizName2, subtext2));
//
//        String quizName3 = getString(R.string.quiz_name_3);
//        String subtext3 = getString(R.string.subtext_3);
//        quizItems.add(new QuizItem(quizName3, subtext3));
//
//        String quizName4 = getString(R.string.quiz_name_4);
//        String subtext4 = getString(R.string.subtext_4);
//        quizItems.add(new QuizItem(quizName4, subtext4));
//
//        String quizName5 = getString(R.string.quiz_name_5);
//        String subtext5 = getString(R.string.subtext_5);
//        quizItems.add(new QuizItem(quizName5, subtext5));
//
//
//        Log.d("QuizItemsRes", "QuizItems: " + quizItems);
//
//        return quizItems;
//    }
}