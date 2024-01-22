package com.example.assignment1;

import static android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_WEAK;
import static android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    TextView textView, announcementTextView;
    ImageSwitcher announcementImageSwitcher;
    private final int[] announcementImages = {R.drawable.books, R.drawable.assembly_announcement, R.drawable.recess_party};
    private final String[] announcementText = {"National Book Day on 25 November!", "Joakim & Sonia this Wednesday!", "Recess Party on 1 December!"};
    private int currentIndex = 0;
    private final Handler handler = new Handler();

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    String[] listItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set the icon selected
        bottomNavigationView.setSelectedItemId(R.id.home);

        // Perform icon selected listener
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.home) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.homework) {
                    startActivity(new Intent(getApplicationContext(), HomeworkPage.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.time) {
                    startActivity(new Intent(getApplicationContext(), QuizPage.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.calendar) {
                    startActivity(new Intent(getApplicationContext(), Timetable.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.form) {
                    startActivity(new Intent(getApplicationContext(), SettingsPage.class));
                    overridePendingTransition(0, 0);
                    return true;
                }

                return false;
            }
        });

        // SOS button click listener
        Button sosButton = findViewById(R.id.SOSButton);
        sosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SOSPage.class);
                startActivity(intent);
            }
        });


//        ANIMATION PROPERTIES
        ImageSwitcher announcementsImages = (ImageSwitcher) findViewById(R.id.imageSwitcherAnnouncement);
        Animation slide = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.side_slide);
        Animation flies = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.flies_up);
        listView = findViewById(R.id.listViewHW);
        announcementsImages.startAnimation(slide);
        listView.startAnimation(slide);

        //GETTING USER'S NAME===================
        String name = User.user_name;
        TextView welcomeText = (TextView) findViewById(R.id.textWelcome);
        welcomeText.setText("Welcome, " + name);
        welcomeText.startAnimation(flies);
        // END OF ANIMATION


        announcementImageSwitcher = findViewById(R.id.imageSwitcherAnnouncement);
        announcementTextView = findViewById(R.id.textViewAnnouncement);
        announcementImageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView myView = new ImageView(getApplicationContext());
                myView.setScaleType(ImageView.ScaleType.FIT_XY);


                return myView;
            }
        });
        announcementImageSwitcher.setInAnimation(AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_in));
        announcementImageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_out));
        announcementImageSwitcher.setImageResource(announcementImages[currentIndex]);
        announcementTextView.setText(announcementText[currentIndex]);
        long interval = 3000L; // 3 seconds
        handler.postDelayed(announcementSwitchRunnable, interval);
        setupHomeworkListView();
        generateHWItems();
    }

    private void setupHomeworkListView() {
        listView = findViewById(R.id.listViewHW);

        List<HomeworkItem> homeworkItems = generateHWItems(); // method to create your list

        HomeworkListAdapter hwAdapter = new HomeworkListAdapter(this, homeworkItems);
        listView.setAdapter(hwAdapter);

    }

    private List<HomeworkItem> generateHWItems() {
        List<HomeworkItem> homeworkItems = new ArrayList<HomeworkItem>() {
            {
                add(new HomeworkItem("Daily Problem Sums", "Mathematics"));
                add(new HomeworkItem("Workbook Page 31-33", "Chinese"));
                add(new HomeworkItem("Exercise 3B", "Science"));
                add(new HomeworkItem("Learn Spelling", "English"));
            }
        };
        return homeworkItems;
    }

    private final Runnable announcementSwitchRunnable = new Runnable() {
        @Override
        public void run() {
            // Update index and switch image
            currentIndex = (currentIndex + 1) % announcementImages.length;
            announcementImageSwitcher.setImageResource(announcementImages[currentIndex]);
            announcementTextView.setText(announcementText[currentIndex]);


            long interval = 3000L; // 3 seconds
            handler.postDelayed(this, interval);
        }
    };

    public void onClickSettings(View v){
        if (v.getId() ==R.id.settings_btn){
            Intent intent = new Intent(this, SettingsPage.class);
            startActivity(intent);
        }
    }

    private void initiateBiometricAuthentication() {

        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Log.e("MY_APP_TAG", "No biometric features available on this device.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                // Prompts the user to create credentials that your app accepts.
                final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_WEAK | DEVICE_CREDENTIAL);
                Toast.makeText(this, "Enroll your biomentrics", Toast.LENGTH_SHORT).show();
                break;
        }

        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(MainActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                                "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
                                Toast.LENGTH_SHORT)
                        .show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setDeviceCredentialAllowed(true)
                .build();

        biometricPrompt.authenticate(promptInfo);
    }

}
