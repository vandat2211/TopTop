package com.example.toptop.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.toptop.Edit_Profile_Activity;
import com.example.toptop.HomeActivity;
import com.example.toptop.Models.MediaObjectt;
import com.example.toptop.Models.userObject;
import com.example.toptop.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile_Fragment extends Fragment {
    TextView tv_phone_users, tv_name_user;
    ImageView avata;
    Button bt_update_profile;
    private userObject user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        tv_phone_users = view.findViewById(R.id.tv_phone_user);
        tv_name_user = view.findViewById(R.id.tv_name_user);
        avata = view.findViewById(R.id.circleImageView_avata);
        bt_update_profile = view.findViewById(R.id.buttonEditProfile);


        showUserInformation();
        bt_update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), Edit_Profile_Activity.class));
                getActivity().finish();

            }
        });
        return view;


    }

    public void showUserInformation() {
        FirebaseUser usert = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(usert.getPhoneNumber());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userObject user = snapshot.getValue(userObject.class);
                tv_name_user.setText(user.getUser_name());
//                Glide.with(getActivity()).load(usert.getPhotoUrl()).error(R.drawable.avatar).into(avata);
                if (usert == null) {
                    return;
                } else {
                    String phone1 = "0";
                    String phone2 = usert.getPhoneNumber().substring(3, 12);
                    String phones = phone1.concat(phone2);
                    phones.trim();
                    tv_phone_users.setText(phones);
                    }



                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        }


}
