package com.example.assignment1;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import com.google.firebase.database.FirebaseDatabase;
public class SplashScreen extends AppCompatActivity {
    private final int SPLASH_DISPLAY = 2000;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setDatabaseUrl("https://andeca2-b5af6-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .setApplicationId("1:590903525065:android:1b622cff39e82e8a631718")
                .setApiKey("AIzaSyDmoAYjWVoJmz0JnSagYf_qMppU93I6bKc")
                .setProjectId("andeca2-b5af6")
                .build();

        FirebaseApp.initializeApp(this, options);
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // Initialize and assign variable
        ImageView image = (ImageView)findViewById(R.id.SplashScreenImage);
        ImageView imagePen = (ImageView)findViewById(R.id.pen);
        ImageView imageStars = (ImageView)findViewById(R.id.stars);
        Animation slide = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.side_slide);
        Animation fliesUp = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.flies_up);

        image.startAnimation(slide);
        imagePen.startAnimation(fliesUp);
        imageStars.startAnimation(fliesUp);


        /* Start the Menu-Activity
         * and close Splash-Screen after SPLAH_DISPLAY milliseconds*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashScreen.this,LoginPage.class);
                SplashScreen.this.startActivity(mainIntent);
                SplashScreen.this.finish();
            }
        }, SPLASH_DISPLAY);
    }
}