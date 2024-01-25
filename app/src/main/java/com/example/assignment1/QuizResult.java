package com.example.assignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class QuizResult extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        Intent intent = getIntent();
        int userScore = intent.getIntExtra("userScore", 0);
        int totalQuestions = intent.getIntExtra("totalQuestions", 0);

        displayResult(userScore, totalQuestions);

        Button backToQuizButton = findViewById(R.id.backToQuizButton);
        backToQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateBackToQuizPage();
            }
        });
    }

    private void displayResult(int userScore, int totalQuestions) {
        TextView resultTextView = findViewById(R.id.resultTextView);
        ImageView below50ImageView = findViewById(R.id.below50ImageView);
        ImageView between50And74ImageView = findViewById(R.id.between50And74ImageView);
        ImageView between75And100ImageView = findViewById(R.id.between75And100ImageView);
        ImageView fullMarksImageView = findViewById(R.id.fullMarksImageView);

        int percentage = (int) (((float) userScore / totalQuestions) * 100);

        String resultMessage = getString(R.string.result_message, userScore, totalQuestions, percentage);
        resultTextView.setText(resultMessage);

        if (percentage < 50) {
            below50ImageView.setVisibility(View.VISIBLE);
        } else if (percentage >= 50 && percentage <= 74) {
            between50And74ImageView.setVisibility(View.VISIBLE);
        } else if (percentage >= 75 && percentage < 100) {
            between75And100ImageView.setVisibility(View.VISIBLE);
        } else if (percentage == 100) {
            fullMarksImageView.setVisibility(View.VISIBLE);
        }
    }

    private void navigateBackToQuizPage() {
        Intent intent = new Intent(QuizResult.this, QuizPage.class);
        startActivity(intent);
        finish();
    }
}