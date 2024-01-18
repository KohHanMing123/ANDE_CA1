package com.example.assignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class QuizDetail extends AppCompatActivity {

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_detail);

        Log.d("Current page", "im inside QuizDetail");
        String quizTitle = getIntent().getStringExtra("quizTitle");

        TextView titleTextView = findViewById(R.id.quizTitle);
        titleTextView.setText(quizTitle);

        // Retrieve quiz questions and options from Firebase for a specific subject
        DatabaseReference quizReference = FirebaseDatabase.getInstance().getReference("Quiz");

        quizReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("QuizDetail", "Quiz DataSnapshot: " + dataSnapshot);

                // Check if the dataSnapshot has child nodes
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot subjectSnapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot quizSnapshot : subjectSnapshot.getChildren()) {
                            String currentQuizTitle = quizSnapshot.child("quiz_title").getValue(String.class);
                            if (quizTitle.equals(currentQuizTitle)) {
                                // Create a TextView for the quiz title
                                TextView quizTitleTextView = new TextView(QuizDetail.this);
                                quizTitleTextView.setText(currentQuizTitle);

                                // Add the quiz title TextView to the layout
                                LinearLayout layout = findViewById(R.id.questionsLayout);
                                layout.addView(quizTitleTextView);

                                // Iterate through quiz questions
                                for (DataSnapshot questionSnapshot : quizSnapshot.child("quiz_questions").getChildren()) {
                                    String questionTitle = questionSnapshot.child("question_title").getValue(String.class);
                                    Log.d("QuizDetail", "Question Title: " + questionTitle);

                                    // Create a TextView for the question
                                    TextView questionTextView = new TextView(QuizDetail.this);
                                    questionTextView.setText(questionTitle);

                                    // Add the question TextView to the layout
                                    layout.addView(questionTextView);

                                    // Iterate through options for each question
                                    for (DataSnapshot optionSnapshot : questionSnapshot.child("options").getChildren()) {
                                        String optionTitle = String.valueOf(optionSnapshot.child("option_title").getValue());
                                        boolean isCorrect = optionSnapshot.child("correct").getValue(Boolean.class);

                                        Log.d("QuizDetail", "Option Title: " + optionTitle + ", Is Correct: " + isCorrect);

                                        // Create a TextView for the option
                                        TextView optionTextView = new TextView(QuizDetail.this);
                                        optionTextView.setText(optionTitle);

                                        layout.addView(optionTextView);

                                        // Optionally, you can customize the UI based on correctness
                                        if (isCorrect) {
                                            optionTextView.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                                        }
                                    }
                                }
                                // Exit the loop once you find the desired quiz
                                break;
                            }
                        }
                    }
                } else {
                    Log.e("QuizDetail", "DataSnapshot is empty or does not have children.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("QuizDetail", "Database Error: " + databaseError.getMessage());
            }
        });
    }
}

// Your Firebase database reference
//        databaseReference = FirebaseDatabase.getInstance().getReference("Quiz");
//
//                // Add a listener to fetch data
//                databaseReference.addValueEventListener(new ValueEventListener() {
//@Override
//public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//        uniqueSubjects.clear(); // Clear the set before adding new items
//
//        // Map to store quizzes organized by subjects
//        quizzesBySubject.clear(); // Clear the class-level variable
//
//        for (DataSnapshot subjectSnapshot : dataSnapshot.getChildren()) {
//        String subjectName = subjectSnapshot.getKey(); // Get subject name
//        uniqueSubjects.add(subjectName);
//        Log.d("FirebaseData", "Subject Name: " + subjectName);
//
//        List<QuizItem> subjectQuizzes = new ArrayList<>();
//
//        for (DataSnapshot quizSnapshot : subjectSnapshot.getChildren()) {
//        String quizTitle = quizSnapshot.child("quiz_title").getValue(String.class);
//        String quizDescription = quizSnapshot.child("quiz_description").getValue(String.class);
//
//        // Log the data
//        Log.d("FirebaseData", "Quiz Title: " + quizTitle);
//        Log.d("FirebaseData", "Quiz Description: " + quizDescription);
//
//        subjectQuizzes.add(new QuizItem(quizTitle, quizDescription, subjectName));
//        }
//
//        // Log the quizzes within the subject
//        Log.d("FirebaseData", "Quizzes for Subject " + subjectName + ": " + subjectQuizzes);
//
//        // Add quizzes for the subject to the map
//        quizzesBySubject.put(subjectName, subjectQuizzes);
//        }
//        Log.d("hello", String.valueOf(quizzesBySubject.keySet()));
//
//        // Update the UI with the retrieved data
//        updateUI(quizzesBySubject, uniqueSubjects);
//        }
//
//@Override
//public void onCancelled(@NonNull DatabaseError databaseError) {
//        // Failed to read value
//        Log.w("FirebaseData", "Failed to read value.", databaseError.toException());
//        }
//        });