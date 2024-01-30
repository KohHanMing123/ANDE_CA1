package com.example.assignment1;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class HomeworkSpinnerAdapter extends BaseAdapter {
    private List<String> homeworkSubjectList;
    private Context context;

    public HomeworkSpinnerAdapter(Context context, List<String> homeworkSubjectList) {

        this.homeworkSubjectList = homeworkSubjectList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return homeworkSubjectList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String subjectName = homeworkSubjectList.get(position);


        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.homework_spinner_item, parent, false);
        }
        TextView textSubjectName = convertView.findViewById(R.id.textSubjectName);
        textSubjectName.setText(subjectName);
        return convertView;
    }
}
