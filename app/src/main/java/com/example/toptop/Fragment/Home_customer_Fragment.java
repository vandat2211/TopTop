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
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.toptop.Adapter.Video_customer_Adapter;
import com.example.toptop.Adapter.Video_profile_Adapter;
import com.example.toptop.HomeActivity;
import com.example.toptop.Models.MediaObjectt;
import com.example.toptop.My_interface.Onclick_Item_Video_profile;
import com.example.toptop.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Home_customer_Fragment extends Fragment {
    public static final String TAG = Home_customer_Fragment.class.getName();
    TextView tv_phone_customer, tv_name_customer;
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
        bt_follow = view.findViewById(R.id.buttonFollow);
        imageViewback=view.findViewById(R.id.imageViewback);
        mhomeActivity = (HomeActivity) getActivity();

        recyclerView1 = view.findViewById(R.id.recyclerVideo_customer);
        mediaObjecttList = new ArrayList<>();
        getdatafromHomeVideoToCustomer();
        adapter = new Video_customer_Adapter(mediaObjecttList, new Onclick_Item_Video_profile() {
            @Override
            public void onClickItemVideo(MediaObjectt media) {
                mhomeActivity.onClickGoToVideoHome_Fragment(media);
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView1.setLayoutManager(gridLayoutManager);
        recyclerView1.setAdapter(adapter);
        return view;
    }

    private void getdatafromHomeVideoToCustomer() {

        Bundle bundle1 = getArguments();
        if (bundle1 != null) {
            MediaObjectt media2 = (MediaObjectt) bundle1.get("MediaObjectt2");
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
        }
        imageViewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getContext(), androidx.appcompat.R.anim.abc_fade_in));
                getParentFragmentManager().popBackStack();
            }
        });

    }
}
