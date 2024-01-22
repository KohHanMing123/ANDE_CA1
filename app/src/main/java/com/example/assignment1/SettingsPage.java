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
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsPage extends AppCompatActivity {
    Switch switcher;
    boolean night_mode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Switch fontSwitcher;
        fontSwitcher = findViewById(R.id.fontSwitcher);
        fontSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fontSwitcher.isChecked()){
                    changeTextSizeForAllTextViews(25);
                } else {
                    changeTextSizeForAllTextViews(16);
                }

            }
        });

        switcher = findViewById(R.id.switchDarkMode);

        sharedPreferences= getSharedPreferences("MODE", Context.MODE_PRIVATE);
        night_mode = sharedPreferences.getBoolean("night", false);
        if (night_mode){
            switcher.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (night_mode){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor= sharedPreferences.edit();
                    editor.putBoolean("night", false);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor= sharedPreferences.edit();
                    editor.putBoolean("night", true);
                }
                editor.apply();
                editor.commit();
            }
        });


        String name = User.user_name;
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String userclass = User.class_name;
        TextView username = (TextView) findViewById(R.id.personal_name);
        username.setText("Name: " + name);

        TextView useremail = (TextView) findViewById(R.id.personal_email);
        useremail.setText("Email: " + email);

        TextView class_text = (TextView) findViewById(R.id.personal_class);
        class_text.setText("Class: " + userclass);

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


    private void changeTextSizeForAllTextViews(int size) {
        // Finding all TextViews by their resource IDs
        int[] textViewIds = {
                R.id.personal_name,
                R.id.personal_email,
                R.id.personal_class,
                R.id.personal_title,
                R.id.switchDarkMode,
                R.id.fontSwitcher,
                R.id.save_settings,
                R.id.switchDarkMode,
                R.id.fontSwitcher,
                R.id.pref_title,
                R.id.dark_mode,
                R.id.font,
                R.id.er_contact,
                R.id.contact,
                R.id.name,
                R.id.relationship
        };

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

}