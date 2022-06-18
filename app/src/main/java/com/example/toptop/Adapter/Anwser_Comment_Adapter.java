package com.example.toptop.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toptop.Anwser_CommentActivity;
import com.example.toptop.Models.Comment;
import com.example.toptop.Models.anwser_Comment;
import com.example.toptop.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Anwser_Comment_Adapter extends RecyclerView.Adapter<Anwser_Comment_Adapter.CommentHoder> {
    Context context;
    List<anwser_Comment> commentList;
    boolean mProcesshearts = false;
    boolean mProcessComment = false;
    String myuId,videoid,CID;

    public Anwser_Comment_Adapter(Context context, List<anwser_Comment> commentList, String myuId, String videoid,String CID) {
        this.context = context;
        this.commentList = commentList;
        this.myuId = myuId;
        this.videoid = videoid;
        this.CID=CID;
    }

    @NonNull
    @Override
    public CommentHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_comments, parent, false);
        return new CommentHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHoder holder, @SuppressLint("RecyclerView") int position) {
//lay du lieu
        String cID_answer=commentList.get(position).getAnswercID();
        String uname=commentList.get(position).getUser_name();
        String uid=commentList.get(position).getUser_id();
        String imgUser=commentList.get(position).getImage_user();
        String comment=commentList.get(position).getComment();
        String time_comment=commentList.get(position).getTime_comment();
        String videoid=commentList.get(position).getVideo_id();
        String cid=commentList.get(position).getCid();
        String countheart_comment=commentList.get(position).getHeart_comment();
        // convert timestamp to dd/mm/yyyy hh:mm am/pm
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(time_comment));
        String datetime = DateFormat.format("dd/MM/yyyy hh:mm aa", cal).toString();
        //set the data
        holder.img_anwser_comment.setVisibility(View.GONE);
        holder.tv_count_comment.setVisibility(View.GONE);
        holder.tvnameuser_comment.setText(uname);
        holder.tvcomments.setText(comment);
        holder.tvtime_comment.setText(datetime);
        holder.count_heart_comment.setText(countheart_comment);
        try {
            Picasso.get().load(imgUser).into(holder.img_user_comment);
        }catch (Exception e){
            Picasso.get().load(R.drawable.avatar).into(holder.img_user_comment);
        }
        holder.img_heartt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hearts = Integer.parseInt(commentList.get(position).getHeart_comment());
                mProcesshearts = true;
                DatabaseReference Refcomments= FirebaseDatabase.getInstance().getReference("videos").child(videoid).child("comments").child(cid).child("anwser_comments");
                DatabaseReference likecomments= FirebaseDatabase.getInstance().getReference().child("likecomments");
                likecomments.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (mProcesshearts) {
                            if (snapshot.child(cID_answer).hasChild(myuId)) {
                                Refcomments.child(cID_answer).child("heart_comment").setValue("" + (hearts - 1));
                                likecomments.child(cID_answer).child(myuId).removeValue();
                                mProcesshearts = false;
                            } else {
                                Refcomments.child(cID_answer).child("heart_comment").setValue("" + (hearts + 1));
                                likecomments.child(cID_answer).child(myuId).setValue("heart");
                                mProcesshearts=false;
                            }

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        sethearts(holder,cID_answer);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(myuId.equals(uid)){
                    //my comment
                    //show delete dialog
                    AlertDialog.Builder builder=new AlertDialog.Builder(v.getRootView().getContext());
                    builder.setTitle("Delete");
                    builder.setMessage("Are you sure to delete this comment?");
                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mProcessComment=true;
                            DatabaseReference ref=FirebaseDatabase.getInstance().getReference("videos").child(videoid);
                            ref.child("comments").child(cid).child("anwser_comments").child(cID_answer).removeValue();
                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(mProcessComment){
                                        String commentss = "" + snapshot.child("video_comment").getValue();
                                        int newCommentVal = Integer.parseInt(commentss) - 1;
                                        ref.child("video_comment").setValue("" + newCommentVal);
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            DatabaseReference refcoment=FirebaseDatabase.getInstance().getReference("videos").child(videoid).child("comments").child(cid);
                            refcoment.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(mProcessComment){
                                        String commentss = "" + snapshot.child("count_comment").getValue();
                                        int newCommentVal = Integer.parseInt(commentss) - 1;
                                        refcoment.child("count_comment").setValue("" + newCommentVal);
                                        mProcessComment = false;
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    });
                    builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }
                else {
                    Toast.makeText(context,"Can't delete this comments!",Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    private void sethearts(CommentHoder holder, String cID) {
        DatabaseReference likecomments= FirebaseDatabase.getInstance().getReference().child("likecomments");
        likecomments.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(cID).hasChild(myuId)){
                    holder.img_heartt_comment.setColorFilter(ContextCompat.getColor(context,R.color.red), PorterDuff.Mode.MULTIPLY);
                }
                else {
                    holder.img_heartt_comment.setColorFilter(ContextCompat.getColor(context,R.color.white), PorterDuff.Mode.MULTIPLY);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class CommentHoder extends RecyclerView.ViewHolder {
        ImageView img_user_comment,img_heartt_comment,img_anwser_comment;
        TextView tvnameuser_comment,tvcomments,tvtime_comment,count_heart_comment,tv_count_comment;
        public CommentHoder(@NonNull View itemView) {
            super(itemView);
            img_user_comment=itemView.findViewById(R.id.imgnameuser_comment);
            tvnameuser_comment=itemView.findViewById(R.id.tv_username_comment);
            tvcomments=itemView.findViewById(R.id.tvcommen);
            tvtime_comment=itemView.findViewById(R.id.tv_time_comment);
            img_heartt_comment=itemView.findViewById(R.id.img_heartt_comment);
            count_heart_comment=itemView.findViewById(R.id.count_heart_comment);
            img_anwser_comment=itemView.findViewById(R.id.img_answer_comment);
            tv_count_comment=itemView.findViewById(R.id.tv_count_comment);
        }
    }
}
