package com.example.assignment1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ExamListAdapter extends BaseAdapter {
    private final Context context;
    private final List<Exam> examItems;

    public ExamListAdapter(Context context, List<Exam> examItems) {
        this.context = context;
        this.examItems = examItems;
    }

    @Override
    public int getCount() {
        return examItems.size();
    }

    @Override
    public Object getItem(int position) {
        return examItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.timetable_list_item, parent, false);
        }

        // replace data to the views
        TextView textViewStartTime = convertView.findViewById(R.id.textStartTime);
        TextView textViewEndTime = convertView.findViewById(R.id.textEndTime);
        TextView textViewEvent = convertView.findViewById(R.id.textEvent);
        TextView textViewVenue = convertView.findViewById(R.id.textVenue);

        Exam examItem = examItems.get(position);

        textViewStartTime.setText(examItem.getStartTime());
        textViewEndTime.setText(examItem.getEndTime());
        textViewEvent.setText(examItem.getTitle());
        textViewVenue.setText(examItem.getVenue());

        return convertView;
    }
}
