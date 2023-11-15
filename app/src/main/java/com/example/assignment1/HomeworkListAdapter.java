package com.example.assignment1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class HomeworkListAdapter extends BaseAdapter {
    private Context context;
    private List<HomeworkItem> homeworkItems;

    public HomeworkListAdapter(Context context, List<HomeworkItem> homeworkItems) {
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
            convertView = inflater.inflate(R.layout.homework_list_item, parent, false);
        }

        // replace data to the views
        TextView textViewHWTitle = convertView.findViewById(R.id.textViewHWTitle);
        TextView textViewHWSubject = convertView.findViewById(R.id.textViewHWSubject);
        ImageView arrowImage = convertView.findViewById(R.id.arrowImage);

        HomeworkItem hwItem = homeworkItems.get(position);
        System.out.println(hwItem.getHWTitle());
        textViewHWTitle.setText(hwItem.getHWTitle());
        textViewHWSubject.setText(hwItem.getHWSubject());
        arrowImage.setImageResource(R.drawable.rightarrow);

        return convertView;
    }
}
