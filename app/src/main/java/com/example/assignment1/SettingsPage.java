package com.example.assignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingsPage extends AppCompatActivity {
    Switch switcher;
    boolean night_mode;
    String selectedRs;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        selectedRs = User.er_relationship;

        Switch fontSwitcher;
        fontSwitcher = findViewById(R.id.fontSwitcher);
        fontSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fontSwitcher.isChecked()) {
                    changeTextSizeForAllTextViews(25);
                } else {
                    changeTextSizeForAllTextViews(16);
                }

            }
        });

        switcher = findViewById(R.id.switchDarkMode);

        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        night_mode = sharedPreferences.getBoolean("night", false);
        if (night_mode) {
            switcher.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (night_mode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("night", false);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("night", true);
                }
                editor.apply();
                editor.commit();
            }
        });

        String name = User.user_name;
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String userclass = User.class_name;
        String erContact = User.phone_no;
        String erName = User.er_name;
        String erRs = User.er_relationship;


        TextView username = (TextView) findViewById(R.id.personal_name);
        username.setText("Name: " + name);

        TextView useremail = (TextView) findViewById(R.id.personal_email);
        useremail.setText("Email: " + email);

        TextView class_text = (TextView) findViewById(R.id.personal_class);
        class_text.setText("Class: " + userclass);

        EditText contact = (EditText) findViewById(R.id.edit_contact);
        contact.setText(erContact);
        EditText er_name = (EditText) findViewById(R.id.edit_er_name);
        er_name.setText(erName);
        setSpinnerSelectedItem(erRs);
    }


    public void onClickLogout(View v) {
        // Check which view was clicked
        if (v.getId() == R.id.logout_btn) {
            Toast.makeText(this, "Log out button was pressed", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();

            // clear the "remember me" preference to prevent instant login after registering
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("rememberMe", false);
            editor.apply();

            Intent intent = new Intent(this, LoginPage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    public void onClickSave(View v) {
        EditText contact = (EditText) findViewById(R.id.edit_contact);
        setupSpinnerListener();

        EditText er_name = (EditText) findViewById(R.id.edit_er_name);

        String newContact = contact.getText().toString();
        String newName = er_name.getText().toString();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Users").child(FirebaseAuth.getInstance().getUid()).child("er_name").setValue(newName);
        mDatabase.child("Users").child(FirebaseAuth.getInstance().getUid()).child("phone_no").setValue(newContact);
        mDatabase.child("Users").child(FirebaseAuth.getInstance().getUid()).child("er_relationship").setValue(selectedRs);

        User.er_name = newName;
        User.er_relationship = selectedRs;
        User.phone_no = newContact;

        Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
    }


    private void changeTextSizeForAllTextViews(int size) {
        // Finding all TextViews by their resource IDs
        int[] textViewIds = {R.id.personal_name, R.id.personal_email, R.id.personal_class, R.id.personal_title, R.id.switchDarkMode, R.id.fontSwitcher, R.id.save_settings, R.id.switchDarkMode, R.id.fontSwitcher, R.id.pref_title, R.id.dark_mode, R.id.font, R.id.er_contact, R.id.contact, R.id.name, R.id.relationship};

        // Iterate through all TextViews and set text size to 20dp
        for (int textViewId : textViewIds) {
            // Find the TextView by its resource ID
            TextView textView = findViewById(textViewId);

            // Check if the TextView is not null before updating
            if (textView != null) {
                // Set the text size to 20dp
                textView.setTextSize(size);
            }
        }
    }

    private void setSpinnerSelectedItem(String desiredItemText) {
        Spinner spinnerRelationship = findViewById(R.id.rs_options);
        String[] relationshipOptions = getResources().getStringArray(R.array.relationship_options);

        // Find the position of the desired item in the array
        int desiredItemPosition = -1;
        for (int i = 0; i < relationshipOptions.length; i++) {
            if (relationshipOptions[i].equals(desiredItemText)) {
                desiredItemPosition = i;
                break;
            }
        }

        if (desiredItemPosition != -1) {
            spinnerRelationship.setSelection(desiredItemPosition);
        } else {
            spinnerRelationship.setSelection(relationshipOptions.length - 1);
        }
    }

    private void setupSpinnerListener() {
        Spinner spinnerRelationship = findViewById(R.id.rs_options);

        spinnerRelationship.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected item value
                selectedRs = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }
}

// Add other methods as needed



