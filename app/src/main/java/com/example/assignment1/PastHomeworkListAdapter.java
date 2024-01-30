package com.example.assignment1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class PastHomeworkListAdapter extends BaseAdapter {

    private final Context context;
    private final List<HomeworkItem> homeworkItems;

    public PastHomeworkListAdapter(Context context, List<HomeworkItem> homeworkItems) {
        this.context = context;
        this.homeworkItems = homeworkItems;
    }

    @Override
    public int getCount() {
        return homeworkItems.size();
    }

    @Override
    public Object getItem(int position) {
        return homeworkItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.past_homework_list_item, parent, false);
        }
        HomeworkItem hwItem = homeworkItems.get(position);
        // replace data to the views
        TextView textViewHWTitle = convertView.findViewById(R.id.textHomeworkName);
        TextView textViewHWDate = convertView.findViewById(R.id.textHomeworkDate);

        textViewHWTitle.setText(hwItem.getHWTitle());
        textViewHWDate.setText(hwItem.getHomeworkDueDate());

        return convertView;
    }
}
