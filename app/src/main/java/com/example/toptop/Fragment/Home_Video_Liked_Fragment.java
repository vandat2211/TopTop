package com.example.toptop.Fragment;

import android.media.MediaCodec;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.toptop.Adapter.videoAdapter;
import com.example.toptop.HomeActivity;
import com.example.toptop.Models.MediaObjectt;
import com.example.toptop.My_interface.Onclick_Item_Video_profile;
import com.example.toptop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Home_Video_Liked_Fragment extends Fragment {
    public static final String TAG2 = Home_video_Fragment.class.getName();
    private ViewPager2 viewPager3;
    private videoAdapter adapter;
    View view;
    private HomeActivity mhomeActivity;
    private List<MediaObjectt> mediaObjecttList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home_video_following,container,false);
        viewPager3=view.findViewById(R.id.viewpager3);
        mhomeActivity=(HomeActivity)getActivity();
        mediaObjecttList=new ArrayList<>();
        getMediafromReatimeDatabase();
        adapter=new videoAdapter(mediaObjecttList, new Onclick_Item_Video_profile() {
            @Override
            public void onClickItemVideo(MediaObjectt media) {
                mhomeActivity.onClickfromHomeVideo_GoToHomeCustomer_Fragment(media);
            }
        }, getContext());
        viewPager3.setAdapter(adapter);
        return view;
    }
    private void getMediafromReatimeDatabase(){
        DatabaseReference dt1=FirebaseDatabase.getInstance().getReference("follows");
        dt1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    MediaObjectt media=ds.getValue(MediaObjectt.class);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseUser fuser= FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myRef=database.getReference("videos");
        Query qr=myRef.orderByChild("like").equalTo("like");
        qr.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MediaObjectt mediaObjectt=snapshot.getValue(MediaObjectt.class);
                if(mediaObjectt!=null){
                    if(!mediaObjectt.getUser_id().equals(fuser.getUid())) {
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
