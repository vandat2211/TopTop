package com.example.toptop.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.example.toptop.Adapter.videoAdapter;
import com.example.toptop.HomeActivity;
import com.example.toptop.Models.MediaObjectt;
import com.example.toptop.My_interface.Onclick_Item_Video_profile;
import com.example.toptop.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Open_Video_Fragment extends Fragment {
    private ViewPager2 viewPager2;
    private videoAdapter adapter;
    ImageView img_back_profile;
    View view;
    private HomeActivity mhomeActivity;
    private List<MediaObjectt> mediaObjecttList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_open_video,container,false);
        viewPager2=view.findViewById(R.id.viewpager_open_video);
        img_back_profile=view.findViewById(R.id.back_profile);
        mhomeActivity=(HomeActivity)getActivity();
        mediaObjecttList=new ArrayList<>();
        getdata();
        getdata2();
        getdata3();
        getdata4();
        adapter=new videoAdapter(mediaObjecttList, new Onclick_Item_Video_profile() {
            @Override
            public void onClickItemVideo(MediaObjectt media) {
                mhomeActivity.onClickfromHomeVideo_GoToHomeCustomer_Fragment(media);
            }
        }, getContext());
        viewPager2.setAdapter(adapter);
        img_back_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getContext(), androidx.appcompat.R.anim.abc_fade_in));
                getParentFragmentManager().popBackStack();

            }
        });
        return view;
    }
    private void getdata(){
        Bundle bundle=getArguments();
        if(bundle!=null){
            MediaObjectt media=(MediaObjectt)bundle.get("MediaObjectt4");
            if (media !=null){
                String key=String.valueOf(media.getVideo_id());
                FirebaseDatabase database=FirebaseDatabase.getInstance();
                DatabaseReference myRef=database.getReference("videos").child(key);
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mediaObjecttList.clear();
                        MediaObjectt media=snapshot.getValue(MediaObjectt.class);
                        mediaObjecttList.add(media);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        }
    }
    private void getdata2(){
        Bundle bundle=getArguments();
        if(bundle!=null){
            MediaObjectt media=(MediaObjectt)bundle.get("MediaObjectt1");
            if (media !=null){
                String key=String.valueOf(media.getVideo_id());
                FirebaseDatabase database=FirebaseDatabase.getInstance();
                DatabaseReference myRef=database.getReference("videos").child(key);
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mediaObjecttList.clear();
                        MediaObjectt media=snapshot.getValue(MediaObjectt.class);
                        mediaObjecttList.add(media);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        }
    }
    private void getdata3(){
        Bundle bundle=getArguments();
        if(bundle!=null){
            MediaObjectt media=(MediaObjectt)bundle.get("MediaObjectt");
            if (media !=null){
                String key=String.valueOf(media.getVideo_id());
                FirebaseDatabase database=FirebaseDatabase.getInstance();
                DatabaseReference myRef=database.getReference("videos").child(key);
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mediaObjecttList.clear();
                        MediaObjectt media=snapshot.getValue(MediaObjectt.class);
                        mediaObjecttList.add(media);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        }
    }
    private void getdata4(){
        Bundle bundle=getArguments();
        if(bundle!=null){
            MediaObjectt media=(MediaObjectt)bundle.get("MediaObjectt5");
            if (media !=null){
                String key=String.valueOf(media.getVideo_id());
                FirebaseDatabase database=FirebaseDatabase.getInstance();
                DatabaseReference myRef=database.getReference("videos").child(key);
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mediaObjecttList.clear();
                        MediaObjectt media=snapshot.getValue(MediaObjectt.class);
                        mediaObjecttList.add(media);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        }
    }


}
