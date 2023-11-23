package com.example.assignment1;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {
    private final int SPLASH_DISPLAY = 2000;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

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
                Intent mainIntent = new Intent(SplashScreen.this,MainActivity.class);
                SplashScreen.this.startActivity(mainIntent);
                SplashScreen.this.finish();
            }
        }, SPLASH_DISPLAY);
    }
}