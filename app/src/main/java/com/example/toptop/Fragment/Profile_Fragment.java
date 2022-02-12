package com.example.toptop.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toptop.Adapter.Video_profile_Adapter;
import com.example.toptop.Edit_Profile_Activity;
import com.example.toptop.HomeActivity;
import com.example.toptop.Models.MediaObjectt;
import com.example.toptop.Models.userObject;
import com.example.toptop.My_interface.OnLongclick_Item_Video_profile;
import com.example.toptop.My_interface.Onclick_Item_Video_profile;
import com.example.toptop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Profile_Fragment extends Fragment {
    public static final String TAG1 = Profile_Fragment.class.getName();
    FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;
    private Video_profile_Adapter adapter;
    private List<MediaObjectt> mediaObjecttList;
    TextView tv_phone_users, tv_name_user, tv_email;
    ImageView avata;
    Button bt_update_profile;
    private userObject user;
    private HomeActivity mhomeActivity;
    FirebaseAuth mauth;
    FirebaseUser usert;
    FirebaseDatabase database;
    DatabaseReference reference;
    String myUid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        tv_phone_users = view.findViewById(R.id.tv_phone_user);
        tv_name_user = view.findViewById(R.id.tv_name_user);
        tv_email = view.findViewById(R.id.tv_email_user);
        avata = view.findViewById(R.id.circleImageView_avata);
        bt_update_profile = view.findViewById(R.id.buttonEditProfile);
        mhomeActivity = (HomeActivity) getActivity();
//
        mauth=FirebaseAuth.getInstance();
        usert=mauth.getCurrentUser();
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("users");
        //
        Query query=reference.orderByChild("user_id").equalTo(usert.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    String name=""+ds.child("user_name").getValue();
                    String phone=""+ds.child("user_phone").getValue();
                    String email=""+ds.child("email").getValue();
                    String img=""+ds.child("profileImage").getValue();
                    //
                    tv_name_user.setText(name);
                    tv_phone_users.setText(phone);
                    tv_email.setText(email);
                    try {
                        Picasso.get().load(img).into(avata);
                    }catch (Exception e){
                        Picasso.get().load(R.drawable.avatar).into(avata);
                    }
//                    Glide.with(getActivity()).load(img).error(R.drawable.avatar).into(avata);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        bt_update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), Edit_Profile_Activity.class));
                getActivity().finish();

            }
        });
        recyclerView = view.findViewById(R.id.recyclerVideo);
        mediaObjecttList = new ArrayList<>();
        getMediafromReatimeDatabase();
        adapter = new Video_profile_Adapter(mediaObjecttList, new Onclick_Item_Video_profile() {
            @Override
            public void onClickItemVideo(MediaObjectt media) {
                mhomeActivity.onClickGoToOpenVideo_Fragment(media);

            }
        }, new OnLongclick_Item_Video_profile() {
            @Override
            public void onClickItemVideo(MediaObjectt media) {
                mhomeActivity.onClickGoToCreate_Fragment(media);
            }
        },getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
        return view;


    }

    private void getMediafromReatimeDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("videos");
        Query qr=myRef.orderByChild("user_id").equalTo(usert.getUid());
        //get all data
        qr.addValueEventListener(new ValueEventListener() {
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
