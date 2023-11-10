package com.example.assignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;

public class LoginPage extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
    }

    @Override
    public void onClick(View v) {
        // Check which view was clicked
        if (v.getId() == R.id.loginButton) {
            // Handle login button click
            // Start MainPage activity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}