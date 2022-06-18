package com.example.toptop.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.toptop.Adapter.Video_customer_Adapter;
import com.example.toptop.HomeActivity;
import com.example.toptop.Models.MediaObjectt;
import com.example.toptop.Models.follower;
import com.example.toptop.Models.userObject;
import com.example.toptop.My_interface.Onclick_Item_Video_profile;
import com.example.toptop.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Home_customer_Fragment2 extends Fragment {
    public static final String TAG = Home_customer_Fragment2.class.getName();
    TextView tv_phone_customer, tv_name_customer,heart_customer,follwer_customer,follwing_customer;
    ImageView avata_customer,imageViewback;
    Button bt_follow;
    private RecyclerView recyclerView1;
    private Video_customer_Adapter adapter;
    private List<MediaObjectt> mediaObjecttList;
    private HomeActivity mhomeActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer, container, false);
        tv_phone_customer = view.findViewById(R.id.tv_phone_customer);
        tv_name_customer = view.findViewById(R.id.tv_name_customer);
        avata_customer = view.findViewById(R.id.circleImageView_avata_customer);
        heart_customer=view.findViewById(R.id.heart_customer);
        follwer_customer=view.findViewById(R.id.follwer_customer);
        follwing_customer=view.findViewById(R.id.follwing_customer);
        bt_follow = view.findViewById(R.id.buttonFollow);
        imageViewback=view.findViewById(R.id.imageViewback);
        mhomeActivity = (HomeActivity) getActivity();

        recyclerView1 = view.findViewById(R.id.recyclerVideo_customer);
        mediaObjecttList = new ArrayList<>();
        getdatafromHomeVideoToCustomer2();
        adapter = new Video_customer_Adapter(mediaObjecttList, new Onclick_Item_Video_profile() {
            @Override
            public void onClickItemVideo(MediaObjectt media) {
                mhomeActivity.onClickItemfromCustomer_GoToOpenVideo_Fragment(media);
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView1.setLayoutManager(gridLayoutManager);
        recyclerView1.setAdapter(adapter);
        imageViewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getContext(), androidx.appcompat.R.anim.abc_fade_in));
                getParentFragmentManager().popBackStack();
                getActivity().overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
            }
        });
        return view;
    }
    private void getdatafromHomeVideoToCustomer2() {

        Bundle bundle1 = getArguments();
        if (bundle1 != null) {
            userObject media2 = (userObject) bundle1.get("MediaObjectt6");
            if (media2 != null) {
                tv_name_customer.setText(media2.getUser_name());
                Glide.with(getActivity()).load(media2.getProfileImage()).error(R.drawable.avatar).into(avata_customer);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("videos");
                Query myq=myRef.orderByChild("user_id").equalTo(media2.getUser_id());
                myq.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mediaObjecttList.clear();
                        for(DataSnapshot ds:snapshot.getChildren()){
                            MediaObjectt media=ds.getValue(MediaObjectt.class);
                            //add to list
                            mediaObjecttList.add(media);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            //
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("videos");
            Query qr=myRef.orderByChild("user_id").equalTo(media2.getUser_id());
            //get all data
            qr.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int sum=0;
                    mediaObjecttList.clear();
                    for(DataSnapshot ds:snapshot.getChildren()){
                        MediaObjectt media=ds.getValue(MediaObjectt.class);
                        //add to list
                        mediaObjecttList.add(media);
                        adapter.notifyDataSetChanged();
                        //
                        //
                        Map<String,Object> map=(Map<String, Object>) ds.getValue();
                        Object plike=map.get("video_heart");
                        int like=Integer.parseInt(String.valueOf(plike));
                        sum +=like;
                        heart_customer.setText(String.valueOf(sum)+"\n"+"thich");

                        //

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            //
            List<follower> followerList=new ArrayList<>();
            DatabaseReference myRef2 = database.getReference("follows").child(media2.getUser_id());
            myRef2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    followerList.clear();
                    follower mfollower=snapshot.getValue(follower.class);
                    if(mfollower!=null) {
                        followerList.add(mfollower);
                        follwer_customer.setText(String.valueOf(followerList.size())+"\n"+"Follower");
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            //
            DatabaseReference myRefff = database.getReference("follows");
            Query qrff=myRefff.orderByChild(media2.getUser_id()).equalTo("1");
            qrff.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int sumfollowing=0;
                    for(DataSnapshot ds:snapshot.getChildren()){
                        Map<String,Object>map1=(Map<String, Object>) ds.getValue();
                        Object following=map1.get(media2.getUser_id());
                        int followingg=Integer.parseInt(String.valueOf(following));
                        sumfollowing +=followingg;
                        follwing_customer.setText(String.valueOf(sumfollowing)+"\n"+"Following");

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


    }



}
