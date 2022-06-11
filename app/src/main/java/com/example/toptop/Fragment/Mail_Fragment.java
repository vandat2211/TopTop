package com.example.toptop.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toptop.Adapter.Chatlist_mail_Adapter;
import com.example.toptop.Adapter.user_mail_Adapter;
import com.example.toptop.HomeActivity;
import com.example.toptop.MainActivity;
import com.example.toptop.Models.Chat;
import com.example.toptop.Models.Chatlist;
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

public class Mail_Fragment extends Fragment {
    public static final String TAG6 = Mail_Fragment.class.getName();
    Toolbar toolbar;
    private ImageButton imgbt_tool_bar;
    SearchView searchView;
    RecyclerView recyclerView_chatlist;
    FirebaseAuth firebaseAuth;
    private HomeActivity mhomeActivity;
    Chatlist_mail_Adapter chatlist_mail_adapter;
    List<userObject> userObjectList;
    List<Chatlist> chatlistList=new ArrayList<>();
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_mail,container,false);
        recyclerView_chatlist=view.findViewById(R.id.recyclerView_mail);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        databaseReference=FirebaseDatabase.getInstance().getReference("ChatList").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    Chatlist chatlist=ds.getValue(Chatlist.class);
                    chatlistList.add(chatlist);
                }
                LoadChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        toolbar=view.findViewById(R.id.toolbar_chat);
        mhomeActivity=(HomeActivity)getActivity();
        imgbt_tool_bar = view.findViewById(R.id.imgbt_tool_bar);
        searchView=view.findViewById(R.id.searchview);
        recyclerView_chatlist.setHasFixedSize(true);
        recyclerView_chatlist.setLayoutManager(new LinearLayoutManager(getActivity()));

        userObjectList=new ArrayList<>();
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgbt_tool_bar.setVisibility(View.INVISIBLE);
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                imgbt_tool_bar.setVisibility(View.VISIBLE);
                return false;
            }
        });
         searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
             @Override
             public boolean onQueryTextSubmit(String query) {
                 chatlist_mail_adapter.getFilter().filter(query);
                 return false;
             }

             @Override
             public boolean onQueryTextChange(String newText) {
                 chatlist_mail_adapter.getFilter().filter(newText);
                 return false;
             }
         });
        checkUserStatus();
        return view;
    }

    private void LoadChats() {
        userObjectList=new ArrayList<>();
        databaseReference=FirebaseDatabase.getInstance().getReference("users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userObjectList.clear();
                for(DataSnapshot ds:snapshot.getChildren()){
                    userObject userObject=ds.getValue(userObject.class);
                    for (Chatlist chatlist: chatlistList){
                        if(userObject.getUser_id() != null && userObject.getUser_id().equals(chatlist.getId())){
                            userObjectList.add(userObject);
                            break;
                        }
                    }
                     chatlist_mail_adapter=new Chatlist_mail_Adapter(getContext(), userObjectList, new Onclick_user_mail() {
                         @Override
                         public void onClickItemUser(com.example.toptop.Models.userObject user) {
                             mhomeActivity.onClickfromHomeVide_GoToHomeCustomer_Fragment(user);
                         }
                     });
                    recyclerView_chatlist.setAdapter(chatlist_mail_adapter);
                    //set last mes
                    for(int i=0;i<userObjectList.size();i++){
                        lastMessage(userObjectList.get(i).getUser_id());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void lastMessage(String user_id) {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String thelastmes="default";
                for(DataSnapshot ds:snapshot.getChildren()){
                    Chat chat=ds.getValue(Chat.class);
                    if(chat==null){
                        continue;
                    }
                    String sender=chat.getSender();
                    String receiver=chat.getReceiver();
                    if(sender==null||receiver==null){
                        continue;
                    }
                    if(chat.getReceiver().equals(firebaseUser.getUid())&&chat.getSender().equals(user_id)||chat.getReceiver().equals(user_id)&&chat.getSender().equals(firebaseUser.getUid())){
                        thelastmes=chat.getMessage();
                    }
                }
            chatlist_mail_adapter.setLastMessageMap(user_id,thelastmes);
                chatlist_mail_adapter.notifyDataSetChanged();
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
