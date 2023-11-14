package com.example.assignment1;

import static java.lang.System.out;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("MainActivity", "Inside onCreate method"); // Log a message

        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set the icon selected
        bottomNavigationView.setSelectedItemId(R.id.dashboard);

        // Perform icon selected listener
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.dashboard) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.time) {
                    startActivity(new Intent(getApplicationContext(), QuizPage.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.edit) {
                    startActivity(new Intent(getApplicationContext(), EditPage.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.settings) {
                    startActivity(new Intent(getApplicationContext(), SettingsPage.class));
                    overridePendingTransition(0, 0);
                    return true;
                }

                return false;
            }
        });
    }
}
