package com.example.mediahub;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//AIzaSyC5GVX24kYESlxPv0iKpsDc6b7TPUN_sto
//Glide was used to load images
public class YoutubeApi {
    private static final String API_KEY = "AIzaSyC5GVX24kYESlxPv0iKpsDc6b7TPUN_sto"; // Replace with your YouTube Data API key
    private static final String BASE_URL = "https://www.googleapis.com/youtube/v3/";

    private OkHttpClient client;
    private Gson gson;

    public YoutubeApi() {
        client = new OkHttpClient();
        gson = new Gson();
    }

    /**
     * Get a list of video links for a given category
     */
    public List<String> getVideosByCategory(String category, int maxResults) {
        List<String> videoLinks = new ArrayList<>();
        String url = BASE_URL + "search?part=snippet&type=video&q=" + category +
                "&maxResults=" + maxResults + "&key=" + API_KEY;

        try {
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                JsonObject json = gson.fromJson(response.body().string(), JsonObject.class);
                JsonArray items = json.getAsJsonArray("items");
                for (int i = 0; i < items.size(); i++) {
                    JsonObject item = items.get(i).getAsJsonObject();
                    String videoId = item.getAsJsonObject("id").get("videoId").getAsString();
                    videoLinks.add("https://www.youtube.com/watch?v=" + videoId);
                }
            }
        } catch (IOException e) {
            Log.e("YouTubeApi", "Error fetching videos: " + e.getMessage());
        }
        return videoLinks;
    }

    /**
     * Get description and view count of a video from its link
     */
    public String getVideoDetails(String videoUrl) {
        String videoId = extractVideoId(videoUrl);
        String url = BASE_URL + "videos?part=snippet,statistics&id=" + videoId + "&key=" + API_KEY;

        try {
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                JsonObject json = gson.fromJson(response.body().string(), JsonObject.class);
                JsonObject item = json.getAsJsonArray("items").get(0).getAsJsonObject();

                String description = item.getAsJsonObject("snippet").get("description").getAsString();
                String views = item.getAsJsonObject("statistics").get("viewCount").getAsString();

                return "Description: " + description + "\nViews: " + views;
            }
        } catch (IOException e) {
            Log.e("YouTubeApi", "Error fetching video details: " + e.getMessage());
        }
        return "No details found";
    }

    /**
     * Get thumbnail URL of a video from its link
     */
    public String getThumbnail(String videoUrl) {
        String videoId = extractVideoId(videoUrl);
        String url = BASE_URL + "videos?part=snippet&id=" + videoId + "&key=" + API_KEY;

        try {
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                JsonObject json = gson.fromJson(response.body().string(), JsonObject.class);
                JsonObject item = json.getAsJsonArray("items").get(0).getAsJsonObject();
                return item.getAsJsonObject("snippet")
                        .getAsJsonObject("thumbnails")
                        .getAsJsonObject("high")
                        .get("url").getAsString();
            }
        } catch (IOException e) {
            Log.e("YouTubeApi", "Error fetching thumbnail: " + e.getMessage());
        }
        return null;
    }

    /**
     * Extract video ID from YouTube URL
     */
    private String extractVideoId(String url) {
        String videoId = null;
        if (url.contains("v=")) {
            videoId = url.substring(url.indexOf("v=") + 2);
            if (videoId.contains("&")) {
                videoId = videoId.substring(0, videoId.indexOf("&"));
            }
        }
        return videoId;
    }
}
