package com.example.assignment1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class TimetableListAdapter extends BaseAdapter {
    private final Context context;
    private final List<TimetableItem> timetableItems;

    public TimetableListAdapter(Context context, List<TimetableItem> timetableItems) {
        this.context = context;
        this.timetableItems = timetableItems;
    }

    @Override
    public int getCount() {
        return timetableItems.size();
    }

    @Override
    public Object getItem(int position) {
        return timetableItems.get(position);
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

        TimetableItem timetableItem = timetableItems.get(position);

        textViewStartTime.setText(timetableItem.getStart_time());
        textViewEndTime.setText(timetableItem.getEnd_time());
        textViewEvent.setText(timetableItem.getEvent());
        textViewVenue.setText(timetableItem.getVenue());

        return convertView;
    }
}
