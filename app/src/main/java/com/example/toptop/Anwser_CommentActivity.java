package com.example.toptop;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.toptop.Adapter.Anwser_Comment_Adapter;
import com.example.toptop.Adapter.Comment_Adapter;
import com.example.toptop.Models.Comment;
import com.example.toptop.Models.anwser_Comment;
import com.example.toptop.Models.userObject;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Anwser_CommentActivity extends AppCompatActivity {
    private EditText ed_comment;
    private ImageButton bt_send_comment;
    ImageView img_user_comment;
    FirebaseUser usert = FirebaseAuth.getInstance().getCurrentUser();
    String username, img_user;
    RecyclerView rcvComment;
    Anwser_Comment_Adapter cmadapter;
    List<anwser_Comment> commentList;
    //
    String CID,hisname_user_comment,imageUrl_user_comment,time_user_comment,comment,videoid,heart_comment_count;
    TextView tvnamecomment, tvcomment, tvtimecoment,tvheart;
    ImageView imgcomment,imgheart_comment;
    boolean mhearts = false;
    int hearts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anwser_comment);
        ed_comment = findViewById(R.id.ed_messenger);
        bt_send_comment = findViewById(R.id.bt_send_comment);
        img_user_comment = findViewById(R.id.img_user_comment);
        //
        tvnamecomment = findViewById(R.id.tv_username_comment);
        tvcomment = findViewById(R.id.tv_user_commen);
        tvtimecoment = findViewById(R.id.tv_time_comment);
        imgcomment = findViewById(R.id.imgnameuser_comment);
        imgheart_comment = findViewById(R.id.img_heartt_comment);
        tvheart=findViewById(R.id.count_heart_comment);
        //
        //get data from Comment_activity
        Intent intent = getIntent();
        CID = intent.getStringExtra("hisCommenId");
        videoid = intent.getStringExtra("hisVideoId");
        heart_comment_count=intent.getStringExtra("hisVideo_heart");
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("videos").child(videoid).child("comments");
        Query query = reference.orderByChild("cID").equalTo(CID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    hisname_user_comment = "" + ds.child("user_name").getValue();
                    comment="" + ds.child("comment").getValue();
                    imageUrl_user_comment = "" + ds.child("image_user").getValue();
                    time_user_comment = "" + ds.child("time_comment").getValue();
                    tvnamecomment.setText(hisname_user_comment);
                    tvcomment.setText(comment);
                    // convert timestamp to dd/mm/yyyy hh:mm am/pm
                    Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                    cal.setTimeInMillis(Long.parseLong(time_user_comment));
                    String datetime = DateFormat.format("dd/MM/yyyy hh:mm aa", cal).toString();
                    tvtimecoment.setText(datetime);
                    try {
                        Picasso.get().load(imageUrl_user_comment).into(imgcomment);
                    } catch (Exception e) {
                        Picasso.get().load(R.drawable.avatar).into(imgcomment);
                    }
//                    Glide.with(Anwser_CommentActivity.this).load(imageUrl_user_comment).error(R.drawable.avatar).into(imgcomment);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //
        rcvComment = findViewById(R.id.rcv_comments);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rcvComment.setLayoutManager(linearLayoutManager);
        rcvComment.setHasFixedSize(true);
        comments();
        LoadComments();
        bt_send_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getdatafromcomment();
            }
        });
        imgheart_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myuId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                hearts = Integer.parseInt(heart_comment_count);
                mhearts = true;
                DatabaseReference Refcomments= FirebaseDatabase.getInstance().getReference("videos").child(videoid).child("comments");
                DatabaseReference likecomments= FirebaseDatabase.getInstance().getReference().child("likecomments");
                likecomments.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (mhearts) {
                            if (snapshot.child(CID).hasChild(myuId)) {
                                Refcomments.child(CID).child("heart_comment").setValue("" + (hearts - 1));
                                likecomments.child(CID).child(myuId).removeValue();
                                mhearts = false;
                            } else {
                                Refcomments.child(CID).child("heart_comment").setValue("" + (hearts + 1));
                                likecomments.child(CID).child(myuId).setValue("heart");
                                mhearts=false;
                            }
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        DatabaseReference dd = FirebaseDatabase.getInstance().getReference("videos").child(videoid).child("comments").child(CID);
        dd.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String commentss_heart = "" + dataSnapshot.child("heart_comment").getValue();
                    tvheart.setText(commentss_heart);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        sethearts(CID);
    }

    boolean mProcessComment = false;

    public void getdatafromcomment() {
        String anwser_comment = ed_comment.getText().toString().trim();
        String timeStamp = String.valueOf(System.currentTimeMillis());
        DatabaseReference dt = FirebaseDatabase.getInstance().getReference("videos").child(videoid).child("comments").child(CID).child("anwser_comments");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("answercID", timeStamp);
        hashMap.put("comment", anwser_comment);
        hashMap.put("user_id", usert.getUid());
        hashMap.put("user_name", username);
        hashMap.put("time_comment", timeStamp);
        hashMap.put("image_user", img_user);
        hashMap.put("heart_comment", "0");
        hashMap.put("video_id", videoid);
        hashMap.put("cid", CID);
        //
        dt.child(timeStamp).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Anwser_CommentActivity.this, "ok", Toast.LENGTH_SHORT).show();
                        ed_comment.setText("");
                        //
                       mProcessComment = true;
                           DatabaseReference dd = FirebaseDatabase.getInstance().getReference("videos").child(videoid).child("comments").child(CID);
                           dd.addValueEventListener(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                   if (mProcessComment) {
                                       String commentss = "" + dataSnapshot.child("count_comment").getValue();
                                       int newCommentVal = Integer.parseInt(commentss) + 1;
                                       dd.child("count_comment").setValue("" + newCommentVal);
                                   }
                               }

                               @Override
                               public void onCancelled(@NonNull DatabaseError databaseError) {

                               }
                           });
                           DatabaseReference ref_count_coment = FirebaseDatabase.getInstance().getReference("videos").child(videoid);
                           ref_count_coment.addValueEventListener(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                   if (mProcessComment) {
                                       String commentss = "" + dataSnapshot.child("video_comment").getValue();
                                       int newCommentVal = Integer.parseInt(commentss) + 1;
                                       ref_count_coment.child("video_comment").setValue("" + newCommentVal);
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
                        Toast.makeText(Anwser_CommentActivity.this, "loi", Toast.LENGTH_SHORT).show();
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
                username = user.getUser_name();
                img_user = user.getProfileImage();
                try {
                    Picasso.get().load(user.getProfileImage()).into(img_user_comment);
                } catch (Exception e) {
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
        overridePendingTransition(R.anim.slide_in_from_down, R.anim.slide_out_to_down);
    }

    private void LoadComments() {
        String myuId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rcvComment.setLayoutManager(linearLayoutManager);
        commentList = new ArrayList<>();
        DatabaseReference rdata = FirebaseDatabase.getInstance().getReference("videos").child(videoid).child("comments").child(CID).child("anwser_comments");
        rdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    anwser_Comment comment = ds.getValue(anwser_Comment.class);
                    commentList.add(comment);

                }
                Collections.reverse(commentList);
                cmadapter = new Anwser_Comment_Adapter(getApplicationContext(), commentList, myuId, videoid,CID);
                rcvComment.setAdapter(cmadapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void sethearts(String cID) {
        String myuId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference likecomments= FirebaseDatabase.getInstance().getReference().child("likecomments");
        likecomments.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(cID).hasChild(myuId)){
                    imgheart_comment.setColorFilter(ContextCompat.getColor(Anwser_CommentActivity.this,R.color.red), PorterDuff.Mode.MULTIPLY);
                }
                else {
                    imgheart_comment.setColorFilter(ContextCompat.getColor(Anwser_CommentActivity.this,R.color.white), PorterDuff.Mode.MULTIPLY);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void back_comment(View view) {
        onBackPressed();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }
}

