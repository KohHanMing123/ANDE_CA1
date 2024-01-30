package com.example.assignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.AsyncListUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class AnnouncementList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView pastRecyclerView;
    private AnnouncementAdapter announcementAdapter;
    private PastAnnouncementAdapter pastAnnouncementAdapter;

    private List<AnnouncementClass> allAnnouncements;
    private List<AnnouncementClass> pastAnnouncements;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String[] filterOptions = {"All","Recommendation ", "Reminder", "Accomplishment"};
        Spinner spinner = findViewById(R.id.filterAnnouncements);

        //annonucements that hasnt been a month
        recyclerView = findViewById(R.id.recyclerViewAnnouncements);
        recyclerView.setLayoutManager(new LinearLayoutManager(AnnouncementList.this));
        announcementAdapter = new AnnouncementAdapter(new ArrayList<>());
        recyclerView.setAdapter(announcementAdapter);

        //past announcement, aka announcement thats past a month
        pastRecyclerView = findViewById(R.id.recyclerViewPastAnnouncements);
        pastRecyclerView.setLayoutManager(new LinearLayoutManager(AnnouncementList.this));
        pastAnnouncementAdapter = new PastAnnouncementAdapter(new ArrayList<>());
        pastRecyclerView.setAdapter(pastAnnouncementAdapter);

        readAnnouncements(new DataCallback<List<AnnouncementClass>>() {

            @Override
            public void onSuccess(List<AnnouncementClass> data) {
                allAnnouncements = data;
                announcementAdapter.updateAnnouncements(allAnnouncements);
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("FIREBASE ERR", "Error getting announcement from Firebase", e);
            }

        });

        readPastAnnouncements(new DataCallback<List<AnnouncementClass>>() {

            @Override
            public void onSuccess(List<AnnouncementClass> data) {
                pastAnnouncements = data;
                pastAnnouncementAdapter.updateAnnouncements(pastAnnouncements);
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("FIREBASE ERR", "Error getting announcement from Firebase", e);
            }

        });
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, filterOptions) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedItem = filterOptions[position];
                Log.d("SPINNER", "Selected Item: " + selectedItem);

                filter(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    public void readAnnouncements(final DataCallback<List<AnnouncementClass>> callback) {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        final List<AnnouncementClass> announcementArr = new ArrayList<>();

        mDatabase.child("Announcement").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.d("taskErr", "109");
                    callback.onFailure(task.getException());
                    return;
                }

                DataSnapshot dataSnapshot = task.getResult();

                // Check if dataSnapshot contains any children
                if (dataSnapshot.hasChildren()) {
                    Log.d("List", "119, onComplete ran");

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Log.d("List", "121, datasnapshot ran");
                        AnnouncementClass announcement = null;



                        HashMap<String, String> announcementData = (HashMap<String, String>) snapshot.getValue();
                        String annTitle = announcementData.get("Title");
                        String annDate = announcementData.get("Date");
                        String annCategory = announcementData.get("Category");
                        String annAuthor = announcementData.get("Author");

                        if (annTitle == null){
                            Log.d("List", "annTitle is null");
                        } else {
                            Log.d("List", "this is annTitle" + annTitle);
                        }

                        boolean pastMonth = false;

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
                        try {
                            LocalDate localDate = LocalDate.parse(annDate, formatter);
                            LocalDate oneMonthLater = localDate.plusMonths(1);
                            LocalDate today = LocalDate.now();

                            pastMonth = today.isAfter(oneMonthLater) || today.isEqual(oneMonthLater);

                        } catch (Exception e) {
                            Log.e("DATE", e.getMessage());
                        }


                        if (!pastMonth){
                            announcement = new AnnouncementClass(annAuthor, annCategory, annDate, annTitle);
                            Log.d("List", "announcementOBJ" + announcement.getCategory());
                        }


                        if (announcement != null) {
                            announcementArr.add(announcement);
                        }
                    }

                    callback.onSuccess(announcementArr);
                } else {
                    Log.d("List", "No children found");
                    callback.onSuccess(new ArrayList<>());
                }
            }
        });
    }


    public void readPastAnnouncements(final DataCallback<List<AnnouncementClass>> callback) {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        final List<AnnouncementClass> announcementArr = new ArrayList<>();

        mDatabase.child("Announcement").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.d("taskErr", "109");
                    callback.onFailure(task.getException());
                    return;
                }

                DataSnapshot dataSnapshot = task.getResult();

                // Check if dataSnapshot contains any children
                if (dataSnapshot.hasChildren()) {
                    Log.d("List", "119, onComplete ran");

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Log.d("List", "121, datasnapshot ran");
                        AnnouncementClass announcement = null;

                        HashMap<String, String> announcementData = (HashMap<String, String>) snapshot.getValue();
                        String annTitle = announcementData.get("Title");
                        String annDate = announcementData.get("Date");
                        String annCategory = announcementData.get("Category");
                        String annAuthor = announcementData.get("Author");

                        if (annTitle == null){
                            Log.d("List", "annTitle is null");
                        } else {
                            Log.d("List", "this is annTitle" + annTitle);
                        }

                        boolean pastMonth = false;

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
                        try {
                            LocalDate localDate = LocalDate.parse(annDate, formatter);
                            LocalDate oneMonthLater = localDate.plusMonths(1);
                            LocalDate today = LocalDate.now();

                            pastMonth = today.isAfter(oneMonthLater) || today.isEqual(oneMonthLater);

                        } catch (Exception e) {
                            Log.e("DATE", e.getMessage());
                        }


                        if (pastMonth){
                            announcement = new AnnouncementClass(annAuthor, annCategory, annDate, annTitle);
                            Log.d("List", "announcementOBJ" + announcement.getCategory());
                        }


                        if (announcement != null) {
                            announcementArr.add(announcement);
                        }
                    }

                    callback.onSuccess(announcementArr);
                } else {
                    Log.d("List", "No children found");
                    callback.onSuccess(new ArrayList<>());
                }
            }
        });
    }



    public interface DataCallback<T> {
        void onSuccess(T data);

        void onFailure(Exception e);
    }

    public void filter(String selectedItem) {
        if (allAnnouncements == null || announcementAdapter == null) {
            Log.d("announcement", "all announcement/adapter is null");
            Toast.makeText(this, "No Announcements", Toast.LENGTH_SHORT).show();
            return;
        }

        List<AnnouncementClass> filteredList = new ArrayList<>();

        for (AnnouncementClass announcement : allAnnouncements) {
            try {
                if (selectedItem.equals("All") || announcement.getCategory().equals(selectedItem)) {
                    filteredList.add(announcement);
                }
            } catch (Exception e){
                Log.e("List", e.toString());
            }
        }

        announcementAdapter.updateAnnouncements(filteredList);
    }


}