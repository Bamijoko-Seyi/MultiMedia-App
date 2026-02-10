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
import java.util.List;

public class YoutubeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_youtube, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.horizontalRecycler);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );



        List<VideoItem> videoList = new ArrayList<>();
        videoList.add(new VideoItem("Action Movie", R.drawable.placeholder_image,"None"));
        videoList.add(new VideoItem("Funny Clip", R.drawable.placeholder_image,"None"));
        videoList.add(new VideoItem("Drama Series", R.drawable.placeholder_image,"None"));

        recyclerView.setAdapter(new VideoAdapter(videoList));

        return view;
    }
}
