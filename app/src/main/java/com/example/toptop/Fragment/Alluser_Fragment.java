package com.example.toptop.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toptop.Adapter.user_mail_Adapter;
import com.example.toptop.HomeActivity;
import com.example.toptop.MainActivity;
import com.example.toptop.Models.userObject;
import com.example.toptop.My_interface.Onclick_user_mail;
import com.example.toptop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Alluser_Fragment extends Fragment {
    public static final String TAG6 = Alluser_Fragment.class.getName();
    Toolbar toolbar;
    private TextView tv_tool_bar;
    SearchView searchView;
    RecyclerView re;
    private HomeActivity mhomeActivity;
    user_mail_Adapter adapter;
    List<userObject> userObjectList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_alluser,container,false);
        re=view.findViewById(R.id.recyclerView_alluser);
        toolbar=view.findViewById(R.id.toolbar_chat);
        mhomeActivity=(HomeActivity)getActivity();
        tv_tool_bar = view.findViewById(R.id.tv_tool_bar);
        searchView=view.findViewById(R.id.searchview);
        re.setHasFixedSize(true);
        re.setLayoutManager(new LinearLayoutManager(getActivity()));

        userObjectList=new ArrayList<>();
        adapter=new user_mail_Adapter(getActivity(), userObjectList, new Onclick_user_mail() {
            @Override
            public void onClickItemUser(userObject user) {
                mhomeActivity.onClickfromHomeVide_GoToHomeCustomer_Fragment(user);
            }
        });
         getAllUsers();
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_tool_bar.setVisibility(View.INVISIBLE);
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                tv_tool_bar.setVisibility(View.VISIBLE);
                return false;
            }
        });
         searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
             @Override
             public boolean onQueryTextSubmit(String query) {
                 adapter.getFilter().filter(query);
                 return false;
             }

             @Override
             public boolean onQueryTextChange(String newText) {
                 adapter.getFilter().filter(newText);
                 return false;
             }
         });
         tv_tool_bar.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 getParentFragmentManager().popBackStack();
             }
         });
        checkUserStatus();
        return view;
    }

    private void getAllUsers() {
        FirebaseUser fuser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userObjectList.clear();
                for(DataSnapshot ds:snapshot.getChildren()){
                    userObject user=ds.getValue(userObject.class);
                    if(!user.getUser_id().equals(fuser.getUid())){
                        userObjectList.add(user);
                    }
                    adapter=new user_mail_Adapter(getActivity(), userObjectList, new Onclick_user_mail() {
                        @Override
                        public void onClickItemUser(userObject user) {
                            mhomeActivity.onClickfromHomeVide_GoToHomeCustomer_Fragment(user);
                        }
                    });
                    re.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void checkUserStatus(){
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user!=null){

        }else{
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
    }


}
