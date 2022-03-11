package com.example.toptop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.toptop.Adapter.Comment_Adapter;
import com.example.toptop.Models.Comment;
import com.example.toptop.Models.MediaObjectt;
import com.example.toptop.Models.userObject;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommentActivity extends AppCompatActivity {
    private EditText ed_comment;
    private ImageButton bt_send_comment;
    ImageView img_user_comment;
    FirebaseUser usert = FirebaseAuth.getInstance().getCurrentUser();
    String username,img_user;
    RecyclerView rcvComment;
    Comment_Adapter cmadapter;
    List<Comment> commentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ed_comment=findViewById(R.id.ed_messenger);
        bt_send_comment=findViewById(R.id.bt_send_comment);
        img_user_comment = findViewById(R.id.img_user_comment);
        rcvComment=findViewById(R.id.rcv_comments);
        comments();
        LoadComments();
        bt_send_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getdatafromcomment();
            }
        });
    }

    boolean mProcessComment = false;
    public void getdatafromcomment(){
        Intent intent=getIntent();
        String videoid=intent.getStringExtra("oj");
        String comment = ed_comment.getText().toString().trim();
                if (TextUtils.isEmpty(comment)) {
                    Toast.makeText(CommentActivity.this, "comment is empty....", Toast.LENGTH_SHORT).show();
                    return;
                }
                String timeStamp = String.valueOf(System.currentTimeMillis());
                DatabaseReference dt = FirebaseDatabase.getInstance().getReference("videos").child(videoid).child("comments");
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("cID", timeStamp);
                hashMap.put("comment", comment);
                hashMap.put("user_id",usert.getUid());
                hashMap.put("user_name", username);
                hashMap.put("time_comment",timeStamp);
                hashMap.put("image_user",img_user);
                hashMap.put("heart_comment","0");
                hashMap.put("video_id",videoid);
                //
                dt.child(timeStamp).setValue(hashMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(CommentActivity.this, "ok", Toast.LENGTH_SHORT).show();
                                ed_comment.setText("");
                                //
                                mProcessComment = true;
                                DatabaseReference dd = FirebaseDatabase.getInstance().getReference("videos").child(videoid);
                                dd.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (mProcessComment) {
                                            String commentss = "" + dataSnapshot.child("video_comment").getValue();
                                            int newCommentVal = Integer.parseInt(commentss) + 1;
                                            dd.child("video_comment").setValue("" + newCommentVal);
                                            mProcessComment = false;
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                //
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CommentActivity.this, "loi", Toast.LENGTH_SHORT).show();
                            }
                        });
                //


            }
    private void comments() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(usert.getUid());
        if (usert == null) {
            return;
        }
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userObject user = snapshot.getValue(userObject.class);
                username=user.getUser_name();
                img_user=user.getProfileImage();
                try {
                    Picasso.get().load(user.getProfileImage()).into(img_user_comment);
                }catch (Exception e){
                    Picasso.get().load(R.drawable.avatar).into(img_user_comment);
                }
//                Glide.with(CommentActivity.this).load(user.getProfileImage()).error(R.drawable.avatar).into(img_user_comment);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void Cancle_comment(View view) {
    onBackPressed();
    overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }
    private void LoadComments(){
        Intent intent=getIntent();
        String videoid=intent.getStringExtra("oj");
        String myuId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        rcvComment.setLayoutManager(linearLayoutManager);
        commentList=new ArrayList<>();
        DatabaseReference rdata=FirebaseDatabase.getInstance().getReference("videos").child(videoid).child("comments");
        rdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentList.clear();
                for (DataSnapshot ds:snapshot.getChildren()){
                    Comment comment=ds.getValue(Comment.class);
                    commentList.add(comment);
                    cmadapter=new Comment_Adapter(getApplicationContext(),commentList,myuId,videoid);
                    rcvComment.setAdapter(cmadapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

