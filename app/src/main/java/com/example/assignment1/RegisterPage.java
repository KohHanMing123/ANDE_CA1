package com.example.assignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class RegisterPage extends AppCompatActivity {

    private EditText usernameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private Spinner classDropdown;
    private Button registerButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        FirebaseAuth.getInstance().signOut();

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        usernameEditText = findViewById(R.id.username_register);
        emailEditText = findViewById(R.id.email_register);
        passwordEditText = findViewById(R.id.password_register);
        confirmPasswordEditText = findViewById(R.id.confirmPassword_register);
        classDropdown = findViewById(R.id.class_dropdown);
        registerButton = findViewById(R.id.registerCreateButton);

        // dummy data for the spinner
        List<String> classOptions = new ArrayList<>();
        classOptions.add("1A");
        classOptions.add("1B");
        classOptions.add("1C");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classDropdown.setAdapter(adapter);

        ImageView registerBackArrow = findViewById(R.id.registerBackArrow);

        registerBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle back arrow click
                onBackPressed();
    //                Intent intent = new Intent(RegisterPage.this, LoginPage.class);
    //                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    registerUser();
                }
            }
        });


    }

    private boolean validateInputs() {
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        String selectedClass = classDropdown.getSelectedItem().toString().trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            // All fields must be filled
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!password.equals(confirmPassword)) {
            // Passwords don't match
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void registerUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Firebase Authentication: Create user with email and password
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String uid = mAuth.getCurrentUser().getUid();
                            String username = usernameEditText.getText().toString().trim();
                            String selectedClass = classDropdown.getSelectedItem().toString().trim();

                            mDatabase.child(uid).child("user_name").setValue(username);
                            mDatabase.child(uid).child("class_name").setValue(selectedClass);

                            mDatabase.child(uid).child("er_name").setValue("Not set");
                            mDatabase.child(uid).child("er_relationship").setValue("Others");
                            mDatabase.child(uid).child("phone_no").setValue("Not set");

                            Toast.makeText(RegisterPage.this, "Successful registration of " + username, Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(RegisterPage.this, LoginPage.class);
                            startActivity(intent);

                        } else {
                            Toast.makeText(RegisterPage.this, "Registration failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}