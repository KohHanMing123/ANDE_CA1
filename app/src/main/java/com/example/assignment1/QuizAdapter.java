package com.example.assignment1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class QuizAdapter extends BaseAdapter {
    private static final String TAG = QuizAdapter.class.getSimpleName();

    private final Context context;
    private final List<QuizItem> quizItems;

    public QuizAdapter(Context context, List<QuizItem> quizItems) {
        this.context = context;
        this.quizItems = quizItems != null ? quizItems : new ArrayList<>();
        Log.d(TAG, "QuizAdapter constructor called with " + getCount() + " items");
    }

    @Override
    public int getCount() {
        return quizItems.size();
    }

    @Override
    public Object getItem(int position) {
        return quizItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.quiz_list_item, parent, false);
        }

        // replace data to the views
        TextView textViewQuizName = convertView.findViewById(R.id.textViewQuizName);
        TextView textViewSubtext = convertView.findViewById(R.id.textViewSubtext);
        ImageView imageViewArrow = convertView.findViewById(R.id.imageViewArrow);

        QuizItem quizItem = quizItems.get(position);

        // Check for null values before setting text
        String quizName = quizItem.getQuizName();
        String quizDescription = quizItem.getQuizDescription();

        if (quizName != null) {
            textViewQuizName.setText(quizName);
        } else {
            Log.e(TAG, "Quiz name is null at position: " + position);
        }

        if (quizDescription != null) {
            textViewSubtext.setText(quizDescription);
        } else {
            Log.e(TAG, "Quiz description is null at position: " + position);
        }

        imageViewArrow.setImageResource(R.drawable.ic_round_play_arrow_24);

        return convertView;
    }

    // Method to update the dataset and notify the adapter
    public void updateData(List<QuizItem> newQuizItems) {
        Log.d(TAG, "Updating data with " + newQuizItems.size() + " items");

        quizItems.clear();
        quizItems.addAll(newQuizItems);

        Log.d(TAG, "Notifying data set changed");
        notifyDataSetChanged();
    }
}
