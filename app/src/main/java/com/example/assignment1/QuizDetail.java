package com.example.assignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

    public class QuizDetail extends AppCompatActivity {
        private String quizTitle;
        private ViewPager viewPager;
        private QuizPagerAdapter adapter;
        private int currentPosition = 0; // to track current question position
        private ProgressBar progressBar;
        private TextView questionCount;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_quiz_detail);

            quizTitle = getIntent().getStringExtra("quizTitle");
            TextView titleTextView = findViewById(R.id.quizTitle);
            titleTextView.setText(quizTitle);

            progressBar = findViewById(R.id.progressBar);
            questionCount = findViewById(R.id.questionCount);
            viewPager = findViewById(R.id.viewPager);

            fetchQuestionsAndSetupUI();

            ImageView backArrow = findViewById(R.id.quizBackArrow);
            backArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish(); // This will finish the current activity and go back to the previous one
                }
            });
        }

        private void fetchQuestionsAndSetupUI() {
            DatabaseReference quizReference = FirebaseDatabase.getInstance().getReference("Quiz");
            quizReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Question> questions = new ArrayList<>();

                    for (DataSnapshot subjectSnapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot quizSnapshot : subjectSnapshot.getChildren()) {
                            String currentQuizTitle = quizSnapshot.child("quiz_title").getValue(String.class);
                            if (quizTitle.equals(currentQuizTitle)) {
                                // Fetch questions for the quiz
                                for (DataSnapshot questionSnapshot : quizSnapshot.child("quiz_questions").getChildren()) {
                                    String questionTitle = questionSnapshot.child("question_title").getValue(String.class);
                                    List<String> options = new ArrayList<>();
                                    boolean[] isCorrectArray = new boolean[(int) questionSnapshot.child("options").getChildrenCount()];

                                    int optionIndex = 0;

                                    for (DataSnapshot optionSnapshot : questionSnapshot.child("options").getChildren()) {
                                        String optionTitle = optionSnapshot.child("option_title").getValue(String.class);
                                        boolean isCorrect = optionSnapshot.child("correct").getValue(Boolean.class); // to check for correct option

                                        Log.d("QuizDetail", "Option Title: " + optionTitle + ", Is Correct: " + isCorrect);

                                        options.add(optionTitle);
                                        isCorrectArray[optionIndex++] = isCorrect;

                                    }
                                    questions.add(new Question(questionTitle, options, isCorrectArray));
                                }
                                break;
                            }
                        }
                    }

                    adapter = new QuizPagerAdapter(getSupportFragmentManager(), questions);
                    viewPager.setAdapter(adapter);
                    setTitleAndNextButton();

                    // Set the initial title
    //                setTitle(currentPosition);

                    updateProgressBar();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("QuizDetail", "Database Error: " + databaseError.getMessage());
                }
            });
        }

        private void setTitleAndNextButton() {
            Button nextButton = findViewById(R.id.nextButton);
            if (nextButton != null) {
                nextButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Check if there is a next question
                        if (currentPosition < adapter.getCount() - 1) {
                            currentPosition++;
                            viewPager.setCurrentItem(currentPosition);
    //                        setTitle(currentPosition);

                            updateProgressBar();
                        } else {
                            Toast.makeText(QuizDetail.this, "No more questions", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
            } else {
                Log.e("QuizDetail", "nextButton is null");
            }
        }

        private void updateProgressBar() {
            int totalQuestions = adapter.getCount();
            int progress = (int) (((float) (currentPosition + 1) / totalQuestions) * 100);
            progressBar.setProgress(progress);
            questionCount.setText("Question " + (currentPosition + 1) + "/" + totalQuestions);
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