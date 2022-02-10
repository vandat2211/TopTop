package com.example.toptop.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.toptop.Adapter.Photo_Viewpager2_Adapter;
import com.example.toptop.Adapter.Search_deltail_Adapter;
import com.example.toptop.Models.Deltail;
import com.example.toptop.Models.MediaObjectt;
import com.example.toptop.Models.Photo;
import com.example.toptop.R;
import com.example.toptop.ZoomOutPageTransformer;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;

public class Search_Fragment extends Fragment {
    private ViewPager2 mviewPager2;
    private CircleIndicator3 circleIndicator3;
    private Handler mhandler=new Handler();
    private Runnable mrunnable=new Runnable() {
        @Override
        public void run() {
            if(mviewPager2.getCurrentItem()==mphotoList.size()-1){
                mviewPager2.setCurrentItem(0);
            }
            else {
            mviewPager2.setCurrentItem(mviewPager2.getCurrentItem()+1);
        }
        }
    };
    private List<Photo> mphotoList;
    private RecyclerView rcv_search_deltail;
    private TextView tv_tool_bar;
    private Search_deltail_Adapter adapter;
    DatabaseReference mydata;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        //init
        //
        mviewPager2 = view.findViewById(R.id.slide_search);
        circleIndicator3 = view.findViewById(R.id.circle_indicator3);
        mphotoList = getlistPhoto();
        Photo_Viewpager2_Adapter photo_viewpager2_adapter = new Photo_Viewpager2_Adapter(mphotoList);
        mviewPager2.setAdapter(photo_viewpager2_adapter);
        circleIndicator3.setViewPager(mviewPager2);
        mviewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mhandler.removeCallbacks(mrunnable);
                mhandler.postDelayed(mrunnable,5000);
            }
        });
        mviewPager2.setPageTransformer(new ZoomOutPageTransformer());
        //
        rcv_search_deltail = view.findViewById(R.id.recyclerView_search_deltail);
        tv_tool_bar = view.findViewById(R.id.tv_tool_bar);
        tv_tool_bar.setText("Kham pha");
        adapter = new Search_deltail_Adapter(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rcv_search_deltail.setLayoutManager(linearLayoutManager);
        adapter.setdata(getListdeltail());
        rcv_search_deltail.setAdapter(adapter);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        mhandler.removeCallbacks(mrunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        mhandler.postDelayed(mrunnable,5000);
    }

    private List<Photo> getlistPhoto() {
        List<Photo> photoList = new ArrayList<>();
        photoList.add(new Photo(R.drawable.photo3));
        photoList.add(new Photo(R.drawable.photo2));
        photoList.add(new Photo(R.drawable.photo1));
        photoList.add(new Photo(R.drawable.photo4));
        return photoList;
    }

    private List<Deltail> getListdeltail() {
        List<Deltail> deltailList = new ArrayList<>();
        List<MediaObjectt> mediaObjecttList = new ArrayList<>();
        mydata = FirebaseDatabase.getInstance().getReference("videos");
        Query query = mydata.orderByChild("hast_task_name").equalTo("#tet");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MediaObjectt mediaObjectt = snapshot.getValue(MediaObjectt.class);
                mediaObjecttList.add(mediaObjectt);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        List<MediaObjectt> mediaObjecttList1 = new ArrayList<>();
        Query query1 = mydata.orderByChild("hast_task_name").equalTo("#hoc");
        query1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MediaObjectt mediaObjectt = snapshot.getValue(MediaObjectt.class);
                mediaObjecttList1.add(mediaObjectt);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        List<MediaObjectt> mediaObjecttList2 = new ArrayList<>();
        Query query2 = mydata.orderByChild("hast_task_name").equalTo("#nha");
        query2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MediaObjectt mediaObjectt = snapshot.getValue(MediaObjectt.class);
                mediaObjecttList2.add(mediaObjectt);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        deltailList.add(new Deltail("#tet", mediaObjecttList));
        deltailList.add(new Deltail("#nha", mediaObjecttList2));
        deltailList.add(new Deltail("#hoc", mediaObjecttList1));
        adapter.notifyDataSetChanged();
        return deltailList;

    }

}
