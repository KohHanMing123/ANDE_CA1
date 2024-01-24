package com.example.assignment1;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment1.AnnouncementClass;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.AnnouncementViewHolder> {
    private List<AnnouncementClass> announcements;

    public AnnouncementAdapter(List<AnnouncementClass> announcements) {
        this.announcements = announcements;
    }

    @NonNull
    @Override
    public AnnouncementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("Class", "AnnouncementViewHolder called");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.announcement_list_item, parent, false);
        return new AnnouncementViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AnnouncementViewHolder holder, int position) {
        AnnouncementClass announcement = announcements.get(position);
        Log.d("Adapter", announcement.getAuthor());
        holder.name.setText(announcement.getTitle());
        holder.date.setText(announcement.getDateString());
        holder.author.setText(announcement.getAuthor());
        holder.category.setText(announcement.getCategory());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        try {
            LocalDate localDate = LocalDate.parse(announcement.getDateString(), formatter);
            LocalDate today = LocalDate.now();

            LocalDate oneWeekLater = localDate.plusWeeks(1);

            if (today.isAfter(oneWeekLater) || today.isEqual(oneWeekLater)) {
                holder.alertDot.setVisibility(View.INVISIBLE);
            }

        } catch (Exception e) {
            Log.e("DATEADAPTER", e.getMessage());
        }

        Log.d("ADAPTER", "onBindViewHolder 64");

    }

    @Override
    public int getItemCount() {
        return announcements.size();
    }

    public void updateAnnouncements(List<AnnouncementClass> newAnnouncements) {
        announcements.clear();
        announcements.addAll(newAnnouncements);
        notifyDataSetChanged();
    }

    public static class AnnouncementViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView date;
        public TextView author;
        public TextView category;
        public ImageView alertDot;

        public AnnouncementViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d("AnnouncementViewHolder", "constructor called");
            this.name = itemView.findViewById(R.id.title);
            this.date = itemView.findViewById(R.id.date);
            this.author = itemView.findViewById(R.id.author);
            this.category = itemView.findViewById(R.id.category);
            this.alertDot = itemView.findViewById(R.id.alertDot);
        }
    }

}
