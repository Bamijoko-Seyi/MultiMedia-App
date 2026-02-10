package com.example.mediahub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {
    public List<String> categories = new ArrayList<>();
    List<String> allVideoLinks = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView youtubeRecyclerView = view.findViewById(R.id.horizontalRecycler);
        RecyclerView tiktokRecyclerView = view.findViewById(R.id.horizontalRecycler2);
        RecyclerView twitchRecyclerView = view.findViewById(R.id.horizontalRecycler3);

        youtubeRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        tiktokRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        twitchRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );

        categories = UserSession.getInstance().getCategories();
        for (String category : categories) {
            List<String> videoLinks = new YoutubeApi().getVideosByCategory(category, 3);
            allVideoLinks.addAll(videoLinks);


        }
        Collections.shuffle(allVideoLinks);
        List<String> randomVideos = allVideoLinks.subList(0, Math.min(5, allVideoLinks.size()));
        List<VideoItem> videoList = new ArrayList<>();
        for (String videoLink : randomVideos) {
            String videoDetails = new YoutubeApi().getVideoDetails(videoLink);
            String thumbnailUrl = new YoutubeApi().getThumbnail(videoLink);
            videoList.add(new VideoItem(videoDetails, thumbnailUrl, videoLink));
        }


        List<VideoItem> placeHoldervideoList = new ArrayList<>();
        placeHoldervideoList.add(new VideoItem("Action Movie", R.drawable.placeholder_image,"None"));
        placeHoldervideoList.add(new VideoItem("Funny Clip", R.drawable.placeholder_image,"None"));
        placeHoldervideoList.add(new VideoItem("Drama Series", R.drawable.placeholder_image,"None"));

        youtubeRecyclerView.setAdapter(new VideoAdapter(videoList));
        tiktokRecyclerView.setAdapter(new VideoAdapter(placeHoldervideoList));
        twitchRecyclerView.setAdapter(new VideoAdapter(placeHoldervideoList));

        return view;
    }
}
