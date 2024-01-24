package com.example.assignment1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ConsentFormListAdapter extends BaseAdapter {
    private final Context context;
    private final List<ConsentFormItem> consentFormItems;

    public ConsentFormListAdapter(Context context, List<ConsentFormItem> consentFormItems) {
        this.context = context;
        this.consentFormItems = consentFormItems;
    }

    @Override
    public int getCount() {
        return consentFormItems.size();
    }

    @Override
    public Object getItem(int position) {
        return consentFormItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list, parent, false);
        }

        // replace data to the views
        TextView textViewTitle = convertView.findViewById(R.id.textConsentTitle);
        TextView textViewDate = convertView.findViewById(R.id.textConsentDate);
        TextView textViewCreatedBy = convertView.findViewById(R.id.textConsentBy);
        TextView textViewVenue = convertView.findViewById(R.id.textVenue);

        ConsentFormItem consentFormItem = consentFormItems.get(position);

        textViewTitle.setText(consentFormItem.getTitle());
        textViewDate.setText(consentFormItem.getDateCreated());
        textViewCreatedBy.setText(consentFormItem.getIssuedBy());

        return convertView;
    }
}
