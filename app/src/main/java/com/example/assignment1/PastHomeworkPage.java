package com.example.assignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PastHomeworkPage extends AppCompatActivity {
    List<String> homeworkSubjectList = new ArrayList<>();
    List<HomeworkItem> homeworkItems = new ArrayList<>();
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_homework_page);
        fetchData();
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PastHomeworkPage.this, HomeworkPage.class));
            }
        });
    }

    private void fetchPastHomeworkItems(String subjectName){
        databaseReference = FirebaseDatabase.getInstance().getReference("Homework").child(User.class_name).child(subjectName);
        homeworkItems.clear();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                dateFormat.setLenient(false);
                Date hwDate;

                Date tdyDate = new Date();
                for(DataSnapshot homeworkSubject: snapshot.getChildren()){

                    try {
                        hwDate = dateFormat.parse(homeworkSubject.child("Due_Date").getValue().toString());
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    if(tdyDate.after(hwDate)){
                        homeworkItems.add(new HomeworkItem(homeworkSubject.getKey().toString(), homeworkSubject.getKey().toString(), homeworkSubject.child("Due_Date").getValue().toString(), homeworkSubject.child("User_Completed").child(User.user_id).getValue(boolean.class)));
                    }
                }
                ListView pastHWLV = findViewById(R.id.lvPastHomework);
                TextView textNPHW = findViewById(R.id.textNoPastHW);
                if(homeworkItems.size() == 0){
                    pastHWLV.setVisibility(View.GONE);
                    textNPHW.setVisibility(View.VISIBLE);
                }else{
                    pastHWLV.setVisibility(View.VISIBLE);
                    textNPHW.setVisibility(View.GONE);
                    renderList();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("a",error.toString());
            }

        });
    }

    private void renderList(){
        ListView pastHWLV = findViewById(R.id.lvPastHomework);

        PastHomeworkListAdapter hwAdapter = new PastHomeworkListAdapter(this, homeworkItems);
        pastHWLV.setAdapter(hwAdapter);
    }
    private void renderSpinner(){
        Spinner spinnerSubject = findViewById(R.id.spinnerSubject);

        HomeworkSpinnerAdapter hwAdapter = new HomeworkSpinnerAdapter(this, homeworkSubjectList);
        spinnerSubject.setAdapter(hwAdapter);
        spinnerSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fetchPastHomeworkItems(homeworkSubjectList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void fetchData(){
        databaseReference = FirebaseDatabase.getInstance().getReference("Homework").child(User.class_name);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot homeworkSubject: snapshot.getChildren()){
                    homeworkSubjectList.add(homeworkSubject.getKey().toString());
                }
                renderSpinner();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("a",error.toString());
            }

        });
    }
}