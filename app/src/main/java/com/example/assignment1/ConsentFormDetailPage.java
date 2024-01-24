package com.example.assignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ConsentFormDetailPage extends AppCompatActivity {
    private String formTitle;
    private ConsentFormItem consentFormDetails;
    private TextView textFormTitle, textFormCreatedBy, textFormContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consent_form_detail_page);

        formTitle = getIntent().getStringExtra("consentFormTitle");
        fetchForm();
    }

    private void fetchForm(){
        DatabaseReference formReference = FirebaseDatabase.getInstance().getReference("Forms");
        formReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot forms : snapshot.getChildren()){
                    if(formTitle.equals(forms.getKey())){
                        String formTitle = forms.getKey();
                        String formContent = forms.child("Content").getValue().toString();
                        String formDate = forms.child("Date").getValue().toString();
                        String formIssuedBy =forms.child("Issued_by").getValue().toString();
                        boolean formIsConsented = forms.child("User_consent").child(User.user_id).getValue(Boolean.class);
                        System.out.println(formIsConsented);
                        consentFormDetails = new ConsentFormItem(formTitle, formContent, formDate, formIssuedBy,formIsConsented);
                    }
                }
                renderFormDetails();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("a",error.toString());
            }
        });
    }

    private void renderFormDetails(){
        textFormTitle = findViewById(R.id.textFormTitle);
        textFormCreatedBy = findViewById(R.id.textFormCreatedBy);
        textFormContent = findViewById(R.id.textFormContent);

        textFormTitle.setText(consentFormDetails.getTitle());
        textFormCreatedBy.setText(consentFormDetails.getIssuedBy());
        textFormContent.setText(consentFormDetails.getContent());
    }
}