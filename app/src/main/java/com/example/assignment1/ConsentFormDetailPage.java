package com.example.assignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
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
    private CardView cardBackground;
    private Button consentButton;

    private boolean isFormAlrConsented = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consent_form_detail_page);
        ImageButton backButton = findViewById(R.id.ibuttonBack);
        cardBackground = findViewById(R.id.cardBackground);
        consentButton = findViewById(R.id.buttonConsent);
        ScrollView scrollViewContent = findViewById(R.id.scrollViewContent);
        //Setting onclick listeners
        consentButton.setOnClickListener(v -> consentForm());
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(ConsentFormDetailPage.this, ConsentFormListPage.class);
            startActivity(intent);
        });
        //Detecting if scrollview is at bottom
        scrollViewContent.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (!scrollViewContent.canScrollVertically(1) && !isFormAlrConsented) {
                    enableConsentButton();
                }
            }
        });


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
                        if(formIsConsented){
                            isFormAlrConsented = true;
                            formAlreadyConsent();
                        }
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

    private void formAlreadyConsent(){
        consentButton.setText("You Have Consented");
        consentButton.setClickable(false);

        cardBackground.setCardBackgroundColor(getColor(R.color.disabled_orange));
    }

    private void renderFormDetails(){
        textFormTitle = findViewById(R.id.textFormTitle);
        textFormCreatedBy = findViewById(R.id.textFormCreatedBy);
        textFormContent = findViewById(R.id.textFormContent);

        textFormTitle.setText(consentFormDetails.getTitle());
        textFormCreatedBy.setText(consentFormDetails.getIssuedBy());
        textFormContent.setText(consentFormDetails.getContent());
    }


    private void consentForm(){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Forms").child(formTitle).child("User_consent").child(User.user_id).setValue(true);
        Intent intent = new Intent(ConsentFormDetailPage.this, ConsentFormListPage.class);
        startActivity(intent);
    }
    private void enableConsentButton(){
        consentButton.setText("Agree");
        cardBackground.setCardBackgroundColor(getColor(R.color.orange));
    }
}