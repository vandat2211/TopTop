package com.example.toptop.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.toptop.Adapter.videoAdapter;
import com.example.toptop.HomeActivity;
import com.example.toptop.Models.MediaObjectt;
import com.example.toptop.My_interface.Onclick_Item_Video_profile;
import com.example.toptop.R;
import com.firebase.ui.database.FirebaseArray;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Home_video_Fragment extends Fragment {
    public static final String TAG2 = Home_video_Fragment.class.getName();
    private ViewPager2 viewPager2;
    private videoAdapter adapter;
    View view;
    private HomeActivity mhomeActivity;
private List<MediaObjectt> mediaObjecttList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home_video,container,false);
        viewPager2=view.findViewById(R.id.viewpager2);
        mhomeActivity=(HomeActivity)getActivity();
        mediaObjecttList=new ArrayList<>();
        getMediafromReatimeDatabase();
        adapter=new videoAdapter(mediaObjecttList, new Onclick_Item_Video_profile() {
            @Override
            public void onClickItemVideo(MediaObjectt media) {
                mhomeActivity.onClickGoToVideoCustomer_Fragment(media);
            }
        }, getContext());
        viewPager2.setAdapter(adapter);
        return view;
    }
    private void getMediafromReatimeDatabase(){
        FirebaseUser fuser= FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myRef=database.getReference("videos");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                MediaObjectt mediaObjectt=snapshot.getValue(MediaObjectt.class);
                if(mediaObjectt!=null){
                    if(!mediaObjectt.getUser_id().equals(fuser.getUid())){
                        mediaObjecttList.add(mediaObjectt);
                        adapter.notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MediaObjectt mediaObjectt=snapshot.getValue(MediaObjectt.class);
                if(mediaObjectt==null || mediaObjecttList==null || mediaObjecttList.isEmpty()){
                    return;
                }
                for (int i=0;i<mediaObjecttList.size();i++){
                    if( mediaObjectt.getVideo_id()==mediaObjecttList.get(i).getVideo_id()){
                        mediaObjecttList.set(i,mediaObjectt);
                    }
                }
                adapter.notifyDataSetChanged();
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
//
    }



}
