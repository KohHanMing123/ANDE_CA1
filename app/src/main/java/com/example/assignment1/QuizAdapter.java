package com.example.assignment1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class QuizAdapter extends BaseAdapter {
    private Context context;
    private List<QuizItem> quizItems;

    public QuizAdapter(Context context, List<QuizItem> quizItems) {
        this.context = context;
        this.quizItems = quizItems;
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
        textViewQuizName.setText(quizItem.getQuizName());
        textViewSubtext.setText(quizItem.getSubtext());
        imageViewArrow.setImageResource(R.drawable.ic_round_play_arrow_24);

        return convertView;
    }

    // Method to update the dataset and notify the adapter
    public void updateData(List<QuizItem> newQuizItems) {
        quizItems.clear();
        quizItems.addAll(newQuizItems);
        notifyDataSetChanged();
    }
}
