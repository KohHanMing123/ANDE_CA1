package com.example.assignment1;

import static android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.biometrics.BiometricManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class LoginPage extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    public static final String user_id = "";
    private SharedPreferences prefs;

    private static final String TAG = "EmailPassword";
    public static final String MyPREFERNCES = "LoginPref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize and assign variable
        setContentView(R.layout.activity_login_page);
        mAuth = FirebaseAuth.getInstance();
        CheckBox rmb_btn = findViewById(R.id.rmb_btn);
        rmb_btn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Save the "remember me" setting in SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("rememberMe", isChecked);
            editor.apply();
        });

        Button registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPage.this, RegisterPage.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        // Check which view was clicked
        if (v.getId() == R.id.loginButton) {
            EditText eEmail = (EditText) findViewById(R.id.email_input);
            String email = eEmail.getText().toString();
            EditText ePwd = (EditText) findViewById(R.id.password_input);
            String password = ePwd.getText().toString();
            try {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    Toast.makeText(LoginPage.this, "Authentication successful.", Toast.LENGTH_SHORT).show();

                                    mDatabase = FirebaseDatabase.getInstance().getReference();
                                    String uId = mAuth.getCurrentUser().getUid();
                                    mDatabase.child("Users").child(uId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            if (!task.isSuccessful()) {
                                                Log.e("firebase", "Error getting data", task.getException());
                                                Toast.makeText(LoginPage.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                                                Intent intent = getIntent();
                                                finish();
                                                startActivity(intent);
                                            } else {
                                                HashMap<String, String> userData = (HashMap<String, String>) task.getResult().getValue();

                                                String erName = userData.get("er_name");
                                                String phoneNo = userData.get("phone_no");
                                                String erRelationship = userData.get("er_relationship");
                                                String userName = userData.get("user_name");
                                                String className = userData.get("class_name");

                                                User user = new User(uId,userName,className, phoneNo, erName, erRelationship);

                                                Toast.makeText(LoginPage.this, "", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(LoginPage.this, MainActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginPage.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            } catch (IllegalArgumentException e) {
                Toast.makeText(this, "Please enter all fields!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean rememberMe = sharedPreferences.getBoolean("rememberMe", false);

        // if user exists and has clicked on remember me, it will log the user in onStart
        if (currentUser != null && rememberMe) {
            Toast.makeText(LoginPage.this, currentUser.getEmail(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(LoginPage.this, "No Authentication", Toast.LENGTH_SHORT).show();
        }
    }

}