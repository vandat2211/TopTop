package com.example.toptop.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.toptop.Adapter.VideoAdapter;
import com.example.toptop.Models.MediaObject;
import com.example.toptop.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class Home_video_Fragment extends Fragment {
    private VideoAdapter adapter;
    private ViewPager2 viewPager2;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_foryou,container,false);
        viewPager2 = view.findViewById(R.id.viewpager2);
        FirebaseRecyclerOptions<MediaObject> options =
                new FirebaseRecyclerOptions.Builder<MediaObject>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("videos"), MediaObject.class)
                        .build();
        adapter = new VideoAdapter(options);
        viewPager2.setAdapter(adapter);
        return view;
    }
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
