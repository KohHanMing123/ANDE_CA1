package com.example.assignment1;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PastAnnouncementAdapter extends RecyclerView.Adapter<PastAnnouncementAdapter.AnnouncementViewHolder> {
    private List<AnnouncementClass> announcements;

    public PastAnnouncementAdapter(List<AnnouncementClass> announcements) {
        this.announcements = announcements;
    }

    public void updateAnnouncements(List<AnnouncementClass> newAnnouncements) {
        announcements.clear();
        announcements.addAll(newAnnouncements);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PastAnnouncementAdapter.AnnouncementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("Class", "AnnouncementViewHolder called");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.announcement_list_item, parent, false);
        return new PastAnnouncementAdapter.AnnouncementViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PastAnnouncementAdapter.AnnouncementViewHolder holder, int position) {
        AnnouncementClass announcement = announcements.get(position);
        holder.name.setText(announcement.getTitle());
        holder.date.setText(announcement.getDateString());
        holder.author.setText(announcement.getAuthor());
        holder.category.setText(announcement.getCategory());
        holder.alertDot.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return announcements.size();
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
