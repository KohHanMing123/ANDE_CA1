package com.example.assignment1;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class HomeworkExpandableListAdapter extends BaseExpandableListAdapter {
    private final Context context;
    private List<String> homeworkSubjectList;
    private HashMap<String, List<HomeworkItem>> homeworkItemsList;

    public HomeworkExpandableListAdapter(Context context, List<String> homeworkSubjectList, HashMap<String, List<HomeworkItem>> homeworkItemsList) {
        this.context = context;
        this.homeworkSubjectList = homeworkSubjectList;
        this.homeworkItemsList = homeworkItemsList;
    }

    @Override
    public int getGroupCount(){
        return this.homeworkSubjectList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition){
        return this.homeworkItemsList.get(this.homeworkSubjectList.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition){
        return this.homeworkSubjectList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition){
        return this.homeworkItemsList.get(this.homeworkSubjectList.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition){
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition){
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String subjectHeader = (String) getGroup(groupPosition);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.homework_expandable_list_item, null);
        }

        TextView textSubjectTitle = convertView.findViewById(R.id.textSubjectTitle);
        textSubjectTitle.setText(subjectHeader);

        ImageView arrowExpand = convertView.findViewById(R.id.imageArrowExpand);
        if(isExpanded){
            arrowExpand.setRotation(270);
        }else{
            arrowExpand.setRotation(180);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        HomeworkItem homeworkItem = (HomeworkItem) getChild(groupPosition, childPosition);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.homework_list_item_child, null);
        }

        TextView textHomeworkTitle = convertView.findViewById(R.id.textHomeworkTitle);
        TextView textHomeworkDueDate = convertView.findViewById(R.id.textHomeworkDueDate);
        CheckBox cbHomeworkDone = convertView.findViewById(R.id.cbHomeworkDone);

        textHomeworkTitle.setText(homeworkItem.getHWTitle());
        textHomeworkDueDate.setText(homeworkItem.getHomeworkDueDate());
        cbHomeworkDone.setChecked(homeworkItem.getHomeworkDone());

        cbHomeworkDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("Homework").child(User.class_name).child(homeworkItem.getHWSubject()).child(homeworkItem.getHWTitle()).child("User_Completed").child(User.user_id).setValue(cbHomeworkDone.isChecked());
               homeworkItem.setHomeworkDone(cbHomeworkDone.isChecked());

            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}
