package com.example.mediahub;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private List<VideoItem> videoList;

    public VideoAdapter(List<VideoItem> videoList) {
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        VideoItem item = videoList.get(position);

        holder.videoTitle.setText(item.getTitle());

        if (item.getThumbnailUrl() != null) {
            // Load from URL
            Glide.with(holder.videoImage.getContext())
                    .load(item.getThumbnailUrl())
                    .placeholder(R.drawable.placeholder_image)
                    .into(holder.videoImage);
        } else {
            // Load from drawable resource
            holder.videoImage.setImageResource(item.getImageResId());
        }
    }

    /*
    * @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        VideoItem video = videoList.get(position);
        holder.videoTitle.setText(video.getTitle());
        holder.videoImage.setImageResource(video.getImageResId());
    }
    * */


    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        ImageView videoImage;
        TextView videoTitle;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            videoImage = itemView.findViewById(R.id.videoImage);
            videoTitle = itemView.findViewById(R.id.videoTitle);
        }
    }
}
