package com.example.assignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class HomeworkPage extends AppCompatActivity {
    static ExpandableListView expandableListView;
    private DatabaseReference databaseReference;
    static HomeworkExpandableListAdapter expandableListViewAdapter;
    private List<String> homeworkSubjectList = new ArrayList<>();
    private HashMap<String, List<HomeworkItem>> homeworkItemList =  new HashMap<String, List<HomeworkItem>>();

    private String subjectClickedOnFromMainPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_page);

        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set the icon selected
        bottomNavigationView.setSelectedItemId(R.id.homework);

        // Perform icon selected listener
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.home) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.homework) {
                    startActivity(new Intent(getApplicationContext(), HomeworkPage.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.time) {
                    startActivity(new Intent(getApplicationContext(), QuizPage.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.calendar) {
                    startActivity(new Intent(getApplicationContext(), Timetable.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.form) {
                    startActivity(new Intent(getApplicationContext(), ConsentFormListPage.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                return false;
            }
        });
        Button btnSeePastHW = findViewById(R.id.buttonSeePastHomework);
        subjectClickedOnFromMainPage = getIntent().getStringExtra("clickedHomeworkSubject");
        btnSeePastHW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeworkPage.this, PastHomeworkPage.class));
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference("Homework").child(User.class_name);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate hwDate;

                int i = 0;
                LocalDate tdyDate = LocalDate.now();
                for(DataSnapshot homeworkSubject: snapshot.getChildren()){
                    homeworkSubjectList.add(homeworkSubject.getKey().toString());
                    List<HomeworkItem> temporaryHomeworkListItems = new ArrayList<>();
                    for(DataSnapshot homeworkItem: homeworkSubject.getChildren()){
                        hwDate = LocalDate.parse(homeworkItem.child("Due_Date").getValue().toString(), dateFormat);
                        if(tdyDate.isBefore(hwDate) || tdyDate.equals(hwDate)){
                            try{
                                temporaryHomeworkListItems.add(new HomeworkItem(homeworkItem.getKey().toString(), homeworkSubject.getKey().toString(), homeworkItem.child("Due_Date").getValue().toString(), homeworkItem.child("User_Completed").child(User.user_id).getValue(boolean.class)));
                            }catch(NullPointerException e){
                                homeworkItem.getRef().child("User_Completed").child(User.user_id).setValue(false);
                                temporaryHomeworkListItems.add(new HomeworkItem(homeworkItem.getKey().toString(), homeworkSubject.getKey().toString(), homeworkItem.child("Due_Date").getValue().toString(), homeworkItem.child("User_Completed").child(User.user_id).getValue(boolean.class)));
                            }
                        }
                    }
                    homeworkItemList.put(homeworkSubject.getKey().toString(), temporaryHomeworkListItems);
                    i++;
                }
                renderExpListView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("a",error.toString());
            }

        });

    }

    private void renderExpListView(){
        expandableListView = findViewById(R.id.homeworkExpandableListView);
        expandableListViewAdapter = new HomeworkExpandableListAdapter(this, homeworkSubjectList, homeworkItemList);
        expandableListView.setAdapter(expandableListViewAdapter);

        if(subjectClickedOnFromMainPage != null){
            expandableListView.expandGroup(homeworkSubjectList.indexOf(subjectClickedOnFromMainPage));
        }
    }

}