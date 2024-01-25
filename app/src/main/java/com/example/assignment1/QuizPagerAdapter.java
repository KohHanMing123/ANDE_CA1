package com.example.assignment1;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QuizPagerAdapter extends FragmentPagerAdapter {

    private List<Question> questions;
    private int userScore;

    public QuizPagerAdapter(FragmentManager fm, List<Question> questions, int userScore) {
        super(fm);
        this.questions = questions;
        this.userScore = userScore;
    }

    @Override
    public Fragment getItem(int position) {
        if (questions != null && position >= 0 && position < questions.size()) {
            Question question = questions.get(position);
            return QuestionFragment.newInstance(question.getQuestionTitle(), question.getOptions(), question.getIsOptionCorrect(), userScore);
        }
        return null;
    }

    @Override
    public int getCount() {
        return (questions != null) ? questions.size() : 0;
    }
}
