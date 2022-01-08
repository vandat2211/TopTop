package com.example.toptop.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.toptop.Adapter.TablayoutAdapter;
import com.example.toptop.Adapter.VideoAdapter;
import com.example.toptop.Models.MediaObject;
import com.example.toptop.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.FirebaseDatabase;

public class Home_Fragment extends Fragment {
       private TabLayout tabLayout;
       private ViewPager2 viewPager2;
       private View mview;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mview=inflater.inflate(R.layout.fragment_home,container,false);
tabLayout=mview.findViewById(R.id.tap_layout);
viewPager2 =mview.findViewById(R.id.home_viewpager);
       TablayoutAdapter adapter=new TablayoutAdapter(this);
       viewPager2.setAdapter(adapter);
//new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
//    switch (position){
//        case 0:
//            tab.setText("For you");
//            break;
//        case 1:
//            tab.setText("Follwing");
//            break;
//
//    }
//}).attach();
        return mview;

    }



}