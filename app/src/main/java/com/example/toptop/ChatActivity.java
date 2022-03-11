package com.example.toptop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.toptop.Adapter.Chat_Adapter;
import com.example.toptop.Models.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {
private Toolbar toolbar;
    private RecyclerView recycler;
    private ImageView imge;
    private TextView name_user_chat,user_Status;
    private EditText messenger;
    private ImageButton bt_send;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference reference;
    //
    ValueEventListener seenListener;
    DatabaseReference userRefForSeen;
    List<Chat> chatList;
    String hisUid,myUid;
    public Chat_Adapter chat_adapter;
    String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //
        toolbar=findViewById(R.id.toolbarrr);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        initdata();
        //
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(linearLayoutManager);
        //
        Intent intent=getIntent();
        hisUid=intent.getStringExtra("hisUid");
        firebaseAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("users");
        Query query=reference.orderByChild("user_id").equalTo(hisUid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    String name=""+ds.child("user_name").getValue();
                    imageUrl=""+ds.child("profileImage").getValue();
                    String typingStatus=""+ds.child("typingTo").getValue();
                    //check typing status
                    if(typingStatus.equals(myUid)){
                        user_Status.setText("Typing....");
                    }
                    else{
                        //get value of onlinestatus
                        String onlineStatus=""+ds.child("onlineStatus").getValue();
                        if(onlineStatus.equals("online")){
                            user_Status.setText(onlineStatus);
                        }
                        else{
                            Calendar cal= Calendar.getInstance(Locale.ENGLISH);
                            cal.setTimeInMillis(Long.parseLong(onlineStatus));
                            String datetime= DateFormat.format("dd/MM/yyyy hh:mm aa",cal).toString();
                            user_Status.setText("Last seen at " +datetime);
                        }
                    }
                    name_user_chat.setText(name);

                    Glide.with(ChatActivity.this).load(imageUrl).error(R.drawable.avatar).into(imge);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mes=messenger.getText().toString().trim();
                if(TextUtils.isEmpty(mes)){
                    Toast.makeText(ChatActivity.this,"ko gui dc khi rong",Toast.LENGTH_SHORT).show();
                }
                else{
                    sendMessage(mes);
                }
            }
        });
        //check edit text change
        messenger.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()==0){
                    checkTypingStatus("noOne");
                }
                else{
                    checkTypingStatus(hisUid);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        readMessage();
        seenMes();
    }

    private void seenMes() {
        userRefForSeen=FirebaseDatabase.getInstance().getReference("chats");
        seenListener=userRefForSeen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    Chat chat=ds.getValue(Chat.class);
                    if(chat.getReceiver().equals(myUid)&&chat.getSender().equals(hisUid)){
                        HashMap<String,Object>SeenhashMap=new HashMap<>();
                        SeenhashMap.put("isSeen",true);
                        ds.getRef().updateChildren(SeenhashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readMessage() {
        chatList=new ArrayList<>();
        DatabaseReference db=FirebaseDatabase.getInstance().getReference("chats");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for(DataSnapshot ds:snapshot.getChildren()){
                    Chat chat=ds.getValue(Chat.class);
                    if(chat.getReceiver().equals(myUid) && chat.getSender().equals(hisUid)||
                            chat.getReceiver().equals(hisUid) && chat.getSender().equals(myUid)){
                        chatList.add(chat);
                    }
                    chat_adapter=new Chat_Adapter(ChatActivity.this,chatList,imageUrl);
                    chat_adapter.notifyDataSetChanged();
                    recycler.setAdapter(chat_adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessage(String mes) {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
        String timestamp= String.valueOf(System.currentTimeMillis());
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("sender",myUid);
        hashMap.put("receiver",hisUid);
        hashMap.put("isSeen",false);
        hashMap.put("timestamp",timestamp);
        hashMap.put("message",mes);
        databaseReference.child("chats").push().setValue(hashMap);

        messenger.setText("");
    }

    private void initdata(){
        recycler=findViewById(R.id.recyler_content_chat);
        name_user_chat=findViewById(R.id.name_chat);
        user_Status=findViewById(R.id.userStatus);
        imge=findViewById(R.id.img_user_chat);
        messenger=findViewById(R.id.messenger);
        bt_send=findViewById(R.id.bt_send);
    }
    private void checkUserStatus(){
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user!=null){
            myUid=user.getUid();
        }else{
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
    private void checkOnlineStatus(String status){
        DatabaseReference dbb=FirebaseDatabase.getInstance().getReference("users").child(myUid);
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("onlineStatus",status);
        //
        dbb.updateChildren(hashMap);
    }
    private void checkTypingStatus(String typing){
        DatabaseReference dbb=FirebaseDatabase.getInstance().getReference("users").child(myUid);
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("typingTo",typing);
        //
        dbb.updateChildren(hashMap);
    }

    @Override
    protected void onStart() {
        checkUserStatus();
        //set online
        checkOnlineStatus("online");
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //set offline
        String timestamp=String.valueOf(System.currentTimeMillis());
        checkOnlineStatus(timestamp);
        checkTypingStatus("noOne");
        userRefForSeen.removeEventListener(seenListener);
    }

    @Override
    protected void onResume() {
        //set online
        checkOnlineStatus("online");
        super.onResume();
    }

    public void back_fragment_mail(View view) {
        onBackPressed();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }
}