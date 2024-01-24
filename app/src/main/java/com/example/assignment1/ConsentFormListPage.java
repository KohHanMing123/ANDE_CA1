package com.example.assignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ConsentFormListPage extends AppCompatActivity {
    private DatabaseReference databaseReference;
    ListView consentFormListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consent_form_list_page);

        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set the icon selected
        bottomNavigationView.setSelectedItemId(R.id.form);

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
                }  else if (itemId == R.id.time) {
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
        renderConsentListView();
    }

    private void renderConsentListView(){
        DatabaseReference formReference = FirebaseDatabase.getInstance().getReference("Forms");
        List<ConsentFormItem> consentFormItems = new ArrayList<>();
        formReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot forms : snapshot.getChildren()){
                    ConsentFormItem newConsentForm = new ConsentFormItem(forms.getKey(), forms.child("Content").getValue().toString(), forms.child("Date").getValue().toString(), forms.child("Issued_by").getValue().toString());
                    consentFormItems.add(newConsentForm);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("a",error.toString());
            }
        });
        consentFormListView = findViewById(R.id.listConsentForm);
        ConsentFormListAdapter consentFormListAdapter = new ConsentFormListAdapter(this, consentFormItems);
        consentFormListView.setAdapter(consentFormListAdapter);

    }
}