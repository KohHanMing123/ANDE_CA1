package com.example.assignment1;

import static android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_WEAK;
import static android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    private SharedPreferences prefs;
    SharedPreferences.Editor editor;
    private DatabaseReference databaseReference;
    TextView textView, announcementTextView;
    ImageSwitcher announcementImageSwitcher;
    private final int[] announcementImages = {R.drawable.books, R.drawable.assembly_announcement, R.drawable.recess_party};
    private String[] announcementText = new String[3];
//    private String[] announcementText = {"R", "A", "R"};
    private DatabaseReference mDatabase;
    private int currentIndex = 0;
    private final Handler handler = new Handler();

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    private  List<HomeworkItem> homeworkItems = new ArrayList<HomeworkItem>();

    String[] listItem;

    @Override
    protected void onStart() {
        super.onStart();
        boolean bioAuth = prefs.getBoolean("bioAuth", false);

        if(!bioAuth){
            initiateBiometricAuthentication();
            editor.putBoolean("bioAuth", true);
            editor.apply();
        }
    }

    @Override
    public void onRestart(){
        editor.putBoolean("bioAuth", false);
        editor.apply();
        super.onRestart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Announcement");

        Query query = mDatabase.orderByChild("Timestamp").limitToLast(3);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> announcementList = new ArrayList<>();

                // Iterate through the top 3 announcements
                for (DataSnapshot announcementSnapshot : dataSnapshot.getChildren()) {
                    try {
                        // Make sure the fields exist and have non-null values
                        String title = announcementSnapshot.child("Title").getValue(String.class);
                        String date = announcementSnapshot.child("Date").getValue(String.class);

                        // Check for null values before processing
                        if (title != null && date != null) {
                            // Combine information into a string
                            String annText = title + " on " + date;
                            announcementList.add(annText);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                String[] announcementArray = announcementList.toArray(new String[0]);
                announcementText = announcementArray;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Toast.makeText(MainActivity.this, "Error retrieving announcements", Toast.LENGTH_SHORT).show();
            }
        });


        prefs=getSharedPreferences(LoginPage.Login, MODE_PRIVATE);
        editor= prefs.edit();

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
                    startActivity(new Intent(getApplicationContext(), ConsentFormListPage.class));
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
        // END OF ANIMATION

        //GETTING USER'S NAME===================
        String name = User.user_name;
        TextView welcomeText = (TextView) findViewById(R.id.textWelcome);
        welcomeText.setText("Welcome, " + name);
        welcomeText.startAnimation(flies);



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
    }
    private void renderHWList(){
        listView = findViewById(R.id.listViewHW);
        HomeworkListAdapter hwAdapter = new HomeworkListAdapter(this, homeworkItems);
        listView.setAdapter(hwAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, HomeworkPage.class);
                intent.putExtra("clickedHomeworkSubject", homeworkItems.get(position).getHWSubject());
                startActivity(intent);
            }
        });
    }

    private void setupHomeworkListView() {
        homeworkItems.clear();
        databaseReference = FirebaseDatabase.getInstance().getReference("Homework").child(User.class_name);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate hwDate;

                int i = 0;
                LocalDate tdyDate = LocalDate.now();
                LocalDate oneWeekDate = LocalDate.now().plusDays(7);

                for(DataSnapshot homeworkSubject: snapshot.getChildren()){
                    for(DataSnapshot homeworkItem: homeworkSubject.getChildren()){
                        hwDate = LocalDate.parse(homeworkItem.child("Due_Date").getValue().toString(), dateFormat);

                        if(tdyDate.isBefore(hwDate) && hwDate.isBefore(oneWeekDate) ){
                            try{
                                homeworkItems.add(new HomeworkItem(homeworkItem.getKey().toString(), homeworkSubject.getKey().toString(), homeworkItem.child("Due_Date").getValue().toString(), homeworkItem.child("User_Completed").child(User.user_id).getValue(boolean.class)));
                            }catch(NullPointerException e){
                                homeworkItem.getRef().child("User_Completed").child(User.user_id).setValue(false);
                                homeworkItems.add(new HomeworkItem(homeworkItem.getKey().toString(), homeworkSubject.getKey().toString(), homeworkItem.child("Due_Date").getValue().toString(), homeworkItem.child("User_Completed").child(User.user_id).getValue(boolean.class)));
                            }
                        }
                    }
                }
                renderHWList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("a",error.toString());
            }

        });
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

    public void onClickAnnoucements(View v){
        Intent intent = new Intent(this, AnnouncementList.class);
        startActivity(intent);
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
                Toast.makeText(this, "Enroll your biometrics for enhanced security", Toast.LENGTH_SHORT).show();
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