package com.example.mediahub;

public class VideoItem {
    private String title;
    private String thumbnailUrl;  // for URL thumbnails
    private int imageResId;       // for drawable resources (placeholders)
    private String videoUrl;

    public VideoItem(String title, int imageResId, String videoUrl) {
        this.title = title;
        this.imageResId = imageResId;
        this.videoUrl = videoUrl;
        this.thumbnailUrl = null;
    }

    public VideoItem(String title, String thumbnailUrl, String videoUrl) {
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.videoUrl = videoUrl;
        this.imageResId = 0; // not used
    }


    public String getTitle() {
        return title;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
}
