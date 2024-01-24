package com.example.assignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class QuizResult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        int userScore = getIntent().getIntExtra("userScore", 0);
        int totalQuestions = getIntent().getIntExtra("totalQuestions", 0);

        // Display the score in your UI
        TextView scoreTextView = findViewById(R.id.scoreTextView);
        scoreTextView.setText("Your Score: " + userScore + "/" + totalQuestions);
    }
}