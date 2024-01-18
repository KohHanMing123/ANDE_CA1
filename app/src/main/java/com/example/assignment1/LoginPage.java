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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPage extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
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
    }

    @Override
    public void onClick(View v) {
        // Check which view was clicked
        if (v.getId() == R.id.loginButton) {
            EditText eEmail = (EditText) findViewById(R.id.email_input);
            String email = eEmail.getText().toString();
            EditText ePwd = (EditText) findViewById(R.id.password_input);
            String password = ePwd.getText().toString();
            try{
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    Toast.makeText(LoginPage.this, "Authentication successful.", Toast.LENGTH_SHORT).show();
                                    // Load the preferences
                                    prefs = getSharedPreferences(MyPREFERNCES, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putString(user_id, mAuth.getUid());
                                    editor.commit();
                                    Intent intent = new Intent(LoginPage.this, MainActivity.class);
                                    startActivity(intent);

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginPage.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            } catch (IllegalArgumentException e){
                Toast.makeText(this, "Please enter all fields!", Toast.LENGTH_SHORT).show();
            } catch (Exception e){
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }

        }
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Toast.makeText(LoginPage.this, currentUser.getEmail(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(LoginPage.this, "No Authentication", Toast.LENGTH_SHORT).show();
        }
    }
    // If remember me was not pressed
    @Override
    public void onDestroy(){
        super.onDestroy();
        //initiate a check box
        CheckBox rmb_btn = (CheckBox) findViewById(R.id.rmb_btn);
        Boolean checkBoxState = rmb_btn.isChecked();

        if (!checkBoxState){
            prefs = getSharedPreferences(MyPREFERNCES, MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            // Store into the SharedPreferences
            editor.putString(user_id,"");
            editor.commit();
            FirebaseAuth.getInstance().signOut();

        }
    }

}